package assignment.game;

/**
 * Custom exception used when invalid move is made
 */
class InvalidMoveException extends Exception
{
    InvalidMoveException(String message)
    {
        super(message);
    }
}
