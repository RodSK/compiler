package jminusminus;

import java.util.ArrayList;

/**
 * The AST node for an Throw Statement.
 */
class JThrowStatement extends JStatement{

    private JExpression expression;

    /**
     * Constructs an AST node for an throw statement.
     *
     * @param line      line in which the throw occurs in the source file.
     */
    public JThrowStatement(int line, JExpression expression) {
        super(line);
        this.expression = expression;
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
        json.addChild("JThrowStatement:" + line, e);

        JSONElement e1 = new JSONElement();
        e.addChild("Expression", e1);
        expression.toJSON(e1);

    }
}


