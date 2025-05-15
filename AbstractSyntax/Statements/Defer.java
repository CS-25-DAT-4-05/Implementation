package AbstractSyntax.Statements;

import java.util.ArrayList;

import AbstractSyntax.SizeParams.SizeParam;
import Lib.Pair;

/* Created 09-05-2025 by Nikolaj
 * 
 * 
 * 
 */

public class Defer implements Stmt {
    public ArrayList<Pair<String,SizeParam>> dim = new ArrayList<Pair<String,SizeParam>>();
    public Stmt stmt;
    public Defer(ArrayList<Pair<String,SizeParam>> dim,Stmt stmt){
        for (Pair<String,SizeParam> pair : dim) {
            this.dim.add(pair);
        }
        this.stmt = stmt;
    }
}
