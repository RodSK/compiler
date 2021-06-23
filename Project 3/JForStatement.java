package jminusminus;

import java.util.ArrayList;

/**
 * The AST node for a For-Statement.
 */
class JForStatement extends JStatement {

    ArrayList<JStatement> forInit;
    JExpression expr;
    ArrayList<JStatement> forUpdate;
    JStatement statement;

    /**
     * Constructs an AST node for an for-statement.
     *
     * @param line       line in which the for-statement occurs in the source file.
     * @param forInit    Init
     * @param expr       Condition
     * @param forUpdate  Update
     * @param statement  Body
     */
    public JForStatement(int line, ArrayList<JStatement> forInit, JExpression expr, ArrayList<JStatement> forUpdate, JStatement statement) {
        super(line);
        this.forInit = forInit;
        this.expr = expr;
        this.forUpdate = forUpdate;
        this.statement = statement;
    }

    /**
     * {@inheritDoc}
     */
    public JStatement analyze(Context context) {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {

    }

    /**
     * {@inheritDoc}
     */
    public void toJSON(JSONElement json) {
        JSONElement e = new JSONElement();
        json.addChild("JForStatement:" + line, e);

        JSONElement e1 = new JSONElement();
        e.addChild("Init", e1);
        forInit.get(0).toJSON(e1);

        JSONElement e2 = new JSONElement();
        e.addChild("Condition", e2);
        expr.toJSON(e2);

        JSONElement e3 = new JSONElement();
        e.addChild("Update", e3);
        forUpdate.get(0).toJSON(e3);

        JSONElement e4 = new JSONElement();
        e.addChild("Body", e4);
        statement.toJSON(e4);

//        if (elsePart != null) {
//            JSONElement e3 = new JSONElement();
//            e.addChild("ElsePart", e3);
//            elsePart.toJSON(e3);
//        }
    }
}
