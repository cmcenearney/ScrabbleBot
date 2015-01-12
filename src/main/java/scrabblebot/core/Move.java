package scrabblebot.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Move takes
 * - a reference to a board state
 * - starting coordinates
 * - direction (DOWN or ACROSS)
 * - word
 * - rack of tiles
 *
 * checks if it is a valid move,
 * and scores it
 */
public class Move {

    private ArrayList<SideWord> sideWords = new ArrayList<SideWord>();
    private int score;
    private final int row;
    private final int column;
    private final Direction direction;
    private final String word;
    private String errorMessage = "error";
    protected Board board;
    boolean intersectsExistingWord = false;
    Dictionary d = Dictionary.INSTANCE;
    private List<Tile> tiles;

    public Move(Board board, int row, int column, Direction direction, String word, List<Tile> tiles) {
        this.row = row;
        this.column = column;
        this.word = word;
        this.direction = direction;
        this.board = board;
        this.tiles = tiles;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getScore() {
        return score;
    }

    private boolean sideWord(String character, int row, int column) {
        String side_word = character;
        int points = 0;

        // look in the positive direction starting one tile over, if occupied, append to side-word, continue
        int pos_ind;
        if (direction == Direction.ACROSS) {
            pos_ind = row + 1;
        } else {
            pos_ind = column + 1;
        }
        BoardSpace next_space;
        while (pos_ind < Board.boardSize) {
            if (direction == Direction.ACROSS) {
                next_space = board.getSpace(pos_ind, column);
            } else {
                next_space = board.getSpace(row, pos_ind);
            }
            if (next_space.isOccupied()) {
                side_word += next_space.getValue();
                points += TileConfig.tile_config.get(next_space.getValue()).points;
                pos_ind++;
            } else {
                break;
            }
        }
        // look in the negative direction starting one tile over, if occupied, prepend to side-word, continue
        int neg_ind;
        if (direction == Direction.ACROSS) {
            neg_ind = row - 1;
        } else {
            neg_ind = column - 1;
        }
        while (neg_ind >= 0) {
            if (direction == Direction.ACROSS) {
                next_space = board.getSpace(neg_ind, column);
            } else {
                next_space = board.getSpace(row, neg_ind);
            }
            if (next_space.isOccupied()) {
                side_word = next_space.getValue() + side_word;
                neg_ind--;
                points += TileConfig.tile_config.get(next_space.getValue()).points;
            } else {
                break;
            }
        }

        if (side_word.length() == 1) {
            return true;
        } else if (side_word.length() > 1 && !d.validWord(side_word) ) {
            return false;
        } else if (side_word.length() > 1 && d.validWord(side_word) ) {
            //score the word
            BoardSpace.Type type = board.getSpace(row, column).getType();
            int multiplicative_factor = 1;
            if (type == BoardSpace.Type.TRIPLE_WORD) {
                multiplicative_factor = 3;
            } else if (type == BoardSpace.Type.DOUBLE_WORD) {
                multiplicative_factor = 2;
            }
            int placed_tile_score = TileConfig.tile_config.get(character).points;
            if (type == BoardSpace.Type.TRIPLE_LETTER) {
                placed_tile_score *= 3;
            } else if (type == BoardSpace.Type.DOUBLE_LETTER) {
                placed_tile_score *= 2;
            }
            points += placed_tile_score;
            points *= multiplicative_factor;
            sideWords.add(new SideWord(side_word, points));
            intersectsExistingWord = true;
            return true;
        }
        return false;
    }

    private List<String> getTileValues(List<Tile> tiles){
        return tiles.stream()
                .map(t -> t.getCharacter())
                .collect(Collectors.toList());
    }

    public boolean checkMove() {
        //first check that it's a word
        if (!d.validWord(word)) {
            errorMessage = "Sorry, '" + word + "' is not a valid word (in our dictionary).";
            return false;
        }
        //if it's the first move make sure it touches the center tile
        if (board.isEmpty()) {
            if (direction == Direction.ACROSS) {
                if (!(row == 7 && column <= 7 && column + word.length() >= 7)) {
                    errorMessage = "Error - the first move must touch the center tile (H,8).";
                    return false;
                }
            } else {
                if (!(column == 7 && row <= 7 && row + word.length() >= 7)) {
                    errorMessage = "Error - the first move must touch the center tile (H,8).";
                    return false;
                }
            }
        }
        //then check that it will work
        boolean tile_placed = false;
        List<String> tile_values = getTileValues(tiles);
        //iterate over the proposed word / board spaces and check at each space/letter that it is possible
        for (int i = 0; i < word.length(); i++) {
            int x = row;
            int y = column;
            String current_letter = Character.toString(word.charAt(i));
            if (direction == Direction.ACROSS) {
                y = column + i;
            } else {
                x = row + i;
            }
            if (y >= Board.boardSize || x >= Board.boardSize) {
                errorMessage = "Sorry, '" + word + "' is too big for that spot.";
                return false;
            }
            BoardSpace current_space = board.getSpace(x, y);
            boolean space_occupied = current_space.isOccupied();
            String current_space_value = current_space.getValue();

            // there is a letter on the space and it's *not* the right letter of the word we're checking
            if (space_occupied && !current_letter.equals(current_space_value)) {
                errorMessage = "there is a letter on the space - " + current_space_value + " and it's *not* the right letter of the word we're checking";
                return false;
            }

            // there is a letter on the space and it *is* the right letter of the word we're checking
            else if (space_occupied && current_letter.equals(current_space_value)) {
                intersectsExistingWord = true;
                continue;
            }

            // the space is empty and player has a tile for the letter
            else if (!space_occupied && tile_values.contains(current_letter)) {
                if (sideWord(current_letter, x, y)) {
                    tile_values.remove(current_letter);
                    tile_placed = true;
                } else {
                    errorMessage = "the space is empty and player has a tile for the letter: " + current_letter;
                    return false;
                }
            }

            // the space is empty and player doesn't have a tile for the letter
            else if (!space_occupied && !tile_values.contains(current_letter)) {
                return false;
            }
        }
        return (tile_placed && ((board.isEmpty()) || intersectsExistingWord));
    }

    public Board makeMove() {
        int score = 0;
        int tiles_placed = 0;
        int multiplicative_factor = 1;
        for (int i = 0; i < word.length(); i++) {
            BoardSpace current_space;
            if (direction == Direction.ACROSS) {
                current_space = board.getSpace(row, (column + i));
            } else {
                current_space = board.getSpace((row + i), column);
            }
            String current_space_value = current_space.getValue();
            String current_letter = Character.toString(word.charAt(i));
            boolean space_occupied = current_space.isOccupied();
            if (!space_occupied) {
                BoardSpace.Type type = current_space.getType();
                current_space.setValue(current_letter);
                tiles_placed++;
                int p = TileConfig.tile_config.get(current_letter).points;
                if (type == BoardSpace.Type.TRIPLE_LETTER) {
                    score += (p * 3);
                } else if (type == BoardSpace.Type.DOUBLE_LETTER) {
                    score += (p * 2);
                } else if (type == BoardSpace.Type.TRIPLE_WORD) {
                    multiplicative_factor *= 3;
                    score += p;
                } else if (type == BoardSpace.Type.DOUBLE_WORD) {
                    multiplicative_factor *= 2;
                    score += p;
                } else if (type == BoardSpace.Type.PLAIN) {
                    score += p;
                }
            } else if (space_occupied) {
                int p = TileConfig.tile_config.get(current_space_value).points;
                score += p;
            }
        }
        score *= multiplicative_factor;
        //is it a 'bingo'?  (must come after multiplying)
        if (tiles_placed == 7) {
            score += 50;
        }
        for (SideWord s : sideWords) {
            score += s.getPoints();
        }
        this.score = score;
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Move move = (Move) o;

        if (column != move.column) return false;
        if (row != move.row) return false;
        if (!board.equals(move.board)) return false;
        if (direction != move.direction) return false;
        if (!tiles.equals(move.tiles)) return false;
        if (!word.equals(move.word)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        result = 31 * result + direction.hashCode();
        result = 31 * result + word.hashCode();
        result = 31 * result + board.hashCode();
        result = 31 * result + tiles.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Move{" +
                "sideWords=" + sideWords +
                ", score=" + score +
                ", row=" + row +
                ", column=" + column +
                ", direction=" + direction +
                ", word='" + word + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", board=" + board +
                ", intersectsExistingWord=" + intersectsExistingWord +
                ", d=" + d +
                ", tiles=" + tiles +
                '}';
    }
}