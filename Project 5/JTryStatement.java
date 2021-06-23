package jminusminus;

import java.util.ArrayList;

/**
 * The AST node for an Try-Catch Statement.
 */
class JTryStatement extends JStatement{

    private JBlock tryBlock;
    private ArrayList<JFormalParameter> formPar;
    private ArrayList<JBlock> catchBlock;
    private JBlock finalBlock;

    /**
     * Constructs an AST node for an try-catch statement.
     *
     * @param line      line in which the break-statement occurs in the source file.
     */
    public JTryStatement(int line, JBlock tryBlock, ArrayList<JFormalParameter> formPar,
                         ArrayList<JBlock> catchBlock, JBlock finalBlock) {
        super(line);
        this.tryBlock = tryBlock;
        this.formPar = formPar;
        this.catchBlock = catchBlock;
        this.finalBlock = finalBlock;
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
        json.addChild("JTryStatement:" + line, e);

        JSONElement e1 = new JSONElement();
        e.addChild("TryBlock", e1);
        tryBlock.toJSON(e1);

        for(int i=0; i<catchBlock.size(); i++){
            JSONElement e2 = new JSONElement();
            e.addChild("CatchBlock", e2);
            catchBlock.get(i).toJSON(e2);

            ArrayList<String> value = new ArrayList<String>();
            value.add("\""+formPar.get(i).name()+"\"");
            value.add("\""+formPar.get(i).type().toString()+"\"");
            e2.addAttribute("parameter", value);
        }

        JSONElement e3 = new JSONElement();
        e.addChild("FinallyBlock", e3);
        finalBlock.toJSON(e3);

    }
}


