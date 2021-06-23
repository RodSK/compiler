package jminusminus;
import static jminusminus.CLConstants.*;

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
        lhs = (JExpression) lhs.analyze(context);
        lhs.type().mustMatchExpected(line(), Type.BOOLEAN);
        rhs1 = (JExpression) rhs1.analyze(context);
        rhs2 = (JExpression) rhs2.analyze(context);
        rhs2.type().mustMatchExpected(line(), rhs1.type());
        type = rhs1.type();
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        String elseLabel = output.createLabel();
        String endLabel = output.createLabel();
        lhs.codegen(output, elseLabel, false);
        rhs1.codegen(output);
        output.addBranchInstruction(GOTO, endLabel);
        output.addLabel(elseLabel);
        rhs2.codegen(output);
        output.addLabel(endLabel);
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
