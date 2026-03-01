package interpreter;

import exceptions.LyraException;
import exceptions.LyraFunctionNotFoundException;
import exceptions.ReturnException;
import nodes.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter {
    private final Map<String, Object> variables = new HashMap<>();
    private final Map<String, FunctionNode> functions = new HashMap<>();


    public Object execute(Node node) {
        if (node instanceof FunctionNode f) {
            functions.put(f.name, f);
        } else if (node instanceof IfNode i) {
            boolean condition = (boolean) execute(i.condition);
            if (condition) {
                return execute(i.thenBranch);
            } else {
                return execute(i.elseBranch);
            }
        } else if (node instanceof ShowNode s) {
            Object value = execute(s.value);
            System.out.println(value);
            return null;
        } else if (node instanceof SendNode s) {
            throw new ReturnException(execute(s.value));
        } else if (node instanceof BinaryNode b) {
            Object left = execute(b.left);
            Object right = execute(b.right);

            if (left instanceof String || right instanceof String) {
                switch (b.operator) {
                    case "+" -> {
                        return left.toString() + right.toString();
                    }
                    case "*" -> {
                        if (left instanceof String && right instanceof Double) {
                            return left.toString().repeat(((Double) right).intValue());
                        }
                        throw new LyraException("Erreur : impossible de multiplier deux textes");
                    }
                    case "-" -> {
                        if (left instanceof String && right instanceof String) {
                            return left.toString().replace(right.toString(), "");
                        }
                        throw new LyraException("Erreur : impossible de soustraire un nombre d'un texte");
                    }
                    default -> throw new LyraException("Erreur : opération impossible sur du texte");
                }
            }

            double l = ((Number) left).doubleValue();
            double r = ((Number) right).doubleValue();


            return switch (b.operator) {
                case ">" -> l > r;
                case "<" -> l < r;
                case ">=" -> l >= r;
                case "<=" -> l <= r;
                case "==" -> l == r;
                case "+" -> l + r;
                case "-" -> l - r;
                case "*" -> l * r;
                case "/" -> l / r;
                default -> throw new RuntimeException("Opérateur inconnu : " + b.operator);
            };
        } else if (node instanceof LiteralNode l) {
            return l.value;
        } else if (node instanceof IdentifierNode id) {
            return variables.get(id.name);
        } else if (node instanceof SetNode s) {
            Object value = execute(s.value);
            variables.put(s.name, value);
        } else if (node instanceof CallNode c) {
            FunctionNode f = functions.get(c.name);
            if (f == null) throw new LyraFunctionNotFoundException(c.name);

            for (int i = 0; i < f.params.size(); i++) {
                variables.put(f.params.get(i), execute(c.args.get(i)));
            }

            try {
                for (Node n : f.body) {
                    execute(n);
                }
            } catch (ReturnException r) {
                return r.value;
            }
            return null;
        }
        return null;
    }

    public void run(List<Node> nodes) {
        for (Node node : nodes) {
            execute(node);
        }
    }

    public void callFunction(String name, List<Object> args) {
        FunctionNode f = functions.get(name);

        // on associe chaque paramètre à sa valeur
        for (int i = 0; i < f.params.size(); i++) {
            variables.put(f.params.get(i), args.get(i));
        }

        // on exécute le body
        for (Node node : f.body) {
            execute(node);
        }
    }
}