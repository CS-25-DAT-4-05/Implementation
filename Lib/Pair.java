package Lib;

/* Created 09-05-2025 by Nikolaj
 * 
 * 
 * 
 */

public class Pair<T1,T2> {
    T1 elem1;
    T2 elem2;
    public Pair(T1 elem1,T2 elem2){
        this.elem1 = elem1;
        this.elem2 = elem2;
    }

    public T1 getElem1() {
        return elem1;
    }

    public T2 getElem2() {
        return elem2;
    }
}
