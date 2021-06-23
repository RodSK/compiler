// Copyright 2012- Bill Campbell, Swami Iyer and Bahar Akbal-Delibas

package jminusminus;

import static jminusminus.CLConstants.*;

/**
 * The AST node for an Long literal.
 */
class JLiteralLong extends JExpression {
    // String representation of the literal.
    private String text;

    /**
     * Constructs an AST node for Long literal given its line number and string representation.
     *
     */
    public JLiteralLong(int line, String text) {
        super(line);
        this.text = text;
    }

    /**
     * {@inheritDoc}
     */
    public JExpression analyze(Context context) {
        type = Type.LONG;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public void codegen(CLEmitter output) {
        long i;
        if(text.charAt(text.length() - 1) == 'L' || text.charAt(text.length() - 1) == 'l'){
            i = Long.parseLong(text.substring(0, text.length() - 1));
        }else{
            i = Long.parseLong(text);
        }

        if (i == 0) {
            output.addNoArgInstruction(LCONST_0);
        }else if(i == 1){
            output.addNoArgInstruction(LCONST_1);
        }else{
            output.addLDCInstruction(i);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void toJSON(JSONElement json) {
        JSONElement e = new JSONElement();
        json.addChild("JLiteralLong:" + line, e);
        e.addAttribute("type", type == null ? "" : type.toString());
        e.addAttribute("value", text);
    }
}
