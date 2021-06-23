package jminusminus;

/**
 * The AST node for a Conditional Expression.
 */
class JConditionalExpression extends JExpression{
    private JExpression lhs;
    private JExpression rhs1;
    private JExpression rhs2;

    /**
     * Constructs an AST node for a conditional-expression.
     *
     * @param line  line in which the expression occurs in the source file.
     * @param lhs   expression.
     * @param rhs1  then part.
     * @param rhs2  else part.
     */
    public JConditionalExpression(int line, JExpression lhs, JExpression rhs1, JExpression rhs2){
        super(line);
        this.lhs = lhs;
        this.rhs1 = rhs1;
        this.rhs2 = rhs2;
    }

    /**
     * {@inheritDoc}
     */
    public JExpression analyze(Context context) {
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
        json.addChild("JConditionalExpression:" + line, e);
        JSONElement e2 = new JSONElement();
        e.addChild("Condition", e2);
        lhs.toJSON(e2);
        JSONElement e3 = new JSONElement();
        e.addChild("ThenPart", e3);
        rhs1.toJSON(e3);
        JSONElement e4 = new JSONElement();
        e.addChild("ElsePart", e4);
        rhs2.toJSON(e4);

    }

}
