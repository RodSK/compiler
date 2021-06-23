package jminusminus;

import static jminusminus.CLConstants.GOTO;

/**
 * The AST node for an Break-Statement.
 */
class JBreakStatement extends JStatement {

    /**
     * Constructs an AST node for an break-statement.
     *
     * @param line      line in which the break-statement occurs in the source file.
     */
    public JBreakStatement(int line) {
        super(line);
    }

    private Object obj;

    /**
     * {@inheritDoc}
     */
    public JStatement analyze(Context context) {
        if (JMember.breakStack.isEmpty()){
            JAST.compilationUnit.reportSemanticError(line,
                    "Incorrect use of Break Statement.");
        }else {
            obj = JMember.breakStack.peek();
        }
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {

            if (obj instanceof JWhileStatement) {
                JWhileStatement classObj = (JWhileStatement) obj;
                output.addBranchInstruction(GOTO, classObj.getBreakLabel());
           }else if(obj instanceof  JSwitchStatement){
                JSwitchStatement classObj = (JSwitchStatement) obj;
                output.addBranchInstruction(GOTO, classObj.getBreakLabel());
            }else if(obj instanceof JDoStatement){
                JDoStatement classObj = (JDoStatement) obj;
                output.addBranchInstruction(GOTO, classObj.getBreakLabel());
            }

    }

    /**
     * {@inheritDoc}
     */
    public void toJSON(JSONElement json) {
        JSONElement e = new JSONElement();
        json.addChild("JBreakStatement:" + line, e);

    }
}

