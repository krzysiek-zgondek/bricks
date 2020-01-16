/**
 * @project Bricks
 * @author SourceOne on 08.11.2019
 */


inline fun (() -> Int).mix(receiver: (Int) -> Int): Int {
    return receiver(this())
}
