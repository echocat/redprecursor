package org.echocat.redprecursor.impl.sun.compilertree;

import com.sun.tools.javac.tree.JCTree;
import org.echocat.redprecursor.compilertree.Operator;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;
import static org.echocat.redprecursor.utils.ContractUtil.requireNonNull;

public class SunOperatorUtil {

    private static final Map<Operator, Integer> OPERATOR_TO_CODE;
    private static final Map<Integer, Operator> CODE_TO_OPERATOR;

    static {
        final Map<Operator, Integer> operatorToCode = new EnumMap<Operator, Integer>(Operator.class);
        final Map<Integer, Operator> codeToOperator = new HashMap<Integer, Operator>();
        registerOperatorWithCode(Operator.ERRONEOUS, JCTree.ERRONEOUS, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.POSITIVE, JCTree.POS, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.NEGATIVE, JCTree.NEG, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.NOT, JCTree.NOT, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.BIT_NOT, JCTree.COMPL, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.PRE_INCREMENT, JCTree.PREINC, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.PRE_DECREMENT, JCTree.PREDEC, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.POST_INCREMENT, JCTree.POSTINC, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.POST_DECREMENT, JCTree.POSTDEC, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.OR, JCTree.OR, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.AND, JCTree.AND, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.BIT_OR, JCTree.BITOR, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.BIT_XOR, JCTree.BITXOR, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.BIT_AND, JCTree.BITAND, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.EQUALS, JCTree.EQ, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.NOT_EQUALS, JCTree.NE, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.LESSER_THAN, JCTree.LT, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.GREATER_THAN, JCTree.GT, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.LESSER_THAN_OR_EQUALS, JCTree.LE, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.GREATER_THAN_OR_EQUALS, JCTree.GE, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.S_LEFT, JCTree.SL, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.S_RIGHT, JCTree.SR, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.US_RIGHT, JCTree.USR, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.PLUS, JCTree.PLUS, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.MINUS, JCTree.MINUS, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.MULTIPLICATION, JCTree.MUL, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.DIVISION, JCTree.DIV, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.MODULO, JCTree.MOD, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.BIT_OR_ASSIGNMENT, JCTree.BITOR_ASG, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.BIT_XOR_ASSIGNMENT, JCTree.BITXOR_ASG, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.BIT_AND_ASSIGNMENT, JCTree.BITAND_ASG, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.S_LEFT_ASSIGNMENT, JCTree.SL_ASG, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.S_RIGHT_ASSIGNMENT, JCTree.SR_ASG, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.US_RIGHT_ASSIGNMENT, JCTree.USR_ASG, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.PLUS_ASSIGNMENT, JCTree.PLUS_ASG, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.MINUS_ASSIGNMENT, JCTree.MINUS_ASG, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.MULTIPLICATION_ASSIGNMENT, JCTree.MUL_ASG, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.DIVISION_ASSIGNMENT, JCTree.DIV_ASG, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.MODULO_ASSIGNMENT, JCTree.MOD_ASG, operatorToCode, codeToOperator);
        registerOperatorWithCode(Operator.LET_EXPRESSION, JCTree.LETEXPR, operatorToCode, codeToOperator);
        OPERATOR_TO_CODE = unmodifiableMap(operatorToCode);
        CODE_TO_OPERATOR = unmodifiableMap(codeToOperator);
    }

    private static void registerOperatorWithCode(Operator operator, int sunCode, Map<Operator, Integer> operatorToCode, Map<Integer, Operator> codeToOperator) {
        operatorToCode.put(operator, sunCode);
        codeToOperator.put(sunCode, operator);
    }

    public static int operatorToCode(Operator operator) {
        requireNonNull("operator", operator);
        final Integer code = OPERATOR_TO_CODE.get(operator);
        if (code == null) {
            throw new IllegalStateException("This system does not know " + operator + ".");
        }
        return code;
    }

    public static Operator codeToOperator(int code) {
        final Operator operator = CODE_TO_OPERATOR.get(code);
        if (operator == null) {
            throw new IllegalArgumentException("This system does not know the code: " + code);
        }
        return operator;
    }

    private SunOperatorUtil() {}
}
