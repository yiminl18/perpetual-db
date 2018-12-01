
package edu.uci.ics.perpetual.expression;

import edu.uci.ics.perpetual.expression.operators.relational.ExpressionList;
import edu.uci.ics.perpetual.expression.operators.relational.NamedExpressionList;
import edu.uci.ics.perpetual.parser.ASTNodeAccessImpl;

/**
 * A function as MAX,COUNT...
 */
public class Function extends ASTNodeAccessImpl implements Expression {

    private String name;
    private ExpressionList parameters;
    private NamedExpressionList namedParameters;
    private boolean allColumns = false;
    private boolean distinct = false;
    private boolean isEscaped = false;
    private String attribute;
    private KeepExpression keep = null;

    @Override
    public void accept(ExpressionVisitor expressionVisitor) {
        expressionVisitor.visit(this);
    }

    /**
     * The name of he function, i.e. "MAX"
     *
     * @return the name of he function
     */
    public String getName() {
        return name;
    }

    public void setName(String string) {
        name = string;
    }

    /**
     * true if the parameter to the function is "*"
     *
     * @return true if the parameter to the function is "*"
     */
    public boolean isAllColumns() {
        return allColumns;
    }

    public void setAllColumns(boolean b) {
        allColumns = b;
    }

    /**
     * true if the function is "distinct"
     *
     * @return true if the function is "distinct"
     */
    public boolean isDistinct() {
        return distinct;
    }

    public void setDistinct(boolean b) {
        distinct = b;
    }

    /**
     * The list of parameters of the function (if any, else null) If the parameter is "*",
     * allColumns is set to true
     *
     * @return the list of parameters of the function (if any, else null)
     */
    public ExpressionList getParameters() {
        return parameters;
    }

    public void setParameters(ExpressionList list) {
        parameters = list;
    }

    /**
     * the parameters might be named parameters, e.g. substring('foobar' from 2 for 3)
     *
     * @return the list of named parameters of the function (if any, else null)
     */

    public NamedExpressionList getNamedParameters() {
        return namedParameters;
    }

    public void setNamedParameters(NamedExpressionList list) {
        namedParameters = list;
    }

    /**
     * Return true if it's in the form "{fn function_body() }"
     *
     * @return true if it's java-escaped
     */
    public boolean isEscaped() {
        return isEscaped;
    }

    public void setEscaped(boolean isEscaped) {
        this.isEscaped = isEscaped;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public KeepExpression getKeep() {
        return keep;
    }

    public void setKeep(KeepExpression keep) {
        this.keep = keep;
    }

    @Override
    public String toString() {
        String params;

        if (parameters != null || namedParameters != null) {
            if(parameters != null){
                params = parameters.toString();
                if (isDistinct()) {
                    params = params.replaceFirst("\\(", "(DISTINCT ");
                } else if (isAllColumns()) {
                    params = params.replaceFirst("\\(", "(ALL ");
                }
            } else{
                params = namedParameters.toString();
            }
        } else if (isAllColumns()) {
            params = "(*)";
        } else {
            params = "()";
        }

        String ans = name + "" + params + "";

        if (attribute != null) {
            ans += "." + attribute;
        }

        if (keep != null) {
            ans += " " + keep.toString();
        }

        if (isEscaped) {
            ans = "{fn " + ans + "}";
        }

        return ans;
    }
}
