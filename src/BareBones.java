import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class BareBones {

    public static class BareBonesRuntime{

        private static class conditionalJump{
            public interface booleanCondition{
                boolean checkCondition();
            }
            private booleanCondition condition;
            private int lineReference;
            public conditionalJump(int lineReference, booleanCondition condition){
                this.lineReference = lineReference;
                this.condition = condition;
            }
            public boolean evaluate(){
                return condition.checkCondition();
            }
            public int getLineNumber(){
                return lineReference;
            }
        }

        public static class programLine{
            private instructions action;
            private String[] parameters;
            public programLine(instructions action, String[] parameters){
                this.action = action;
                this.parameters = parameters;
            }
            void runLine(BareBonesRuntime runtime){
                action.runAction(runtime, parameters);
            }
        }

        private Stack<conditionalJump> conditionalJumps = new Stack<>();
        private HashMap<String, Integer> CurrentVars = new HashMap<>();
        private static HashMap<String, instructions> instructionsHashMap = new HashMap<>();
        private static HashMap<String, comparators> comparatorsHashMap = new HashMap<>();
        private int programCounter;
        private programLine[] programLines;
        private String[] FinalVarsNames;
        private int[] FinalVarsValues;

        public BareBonesRuntime(String[] lines){
            instructions.values();
            programLine[] programLines = new programLine[lines.length];
            for(int i = 0; i < lines.length; i++){
                String[] line = lines[i].split(" +");
                String[] parameters = new String[line.length-1];
                for(int j = 0; j < parameters.length; j++) parameters[j] = line[j+1];

                /*System.out.println("Loading instruction = " + lines[i]);
                System.out.print("Instruction : "+ line[0]+"\nParameters : ");
                for(int j = 0; j < parameters.length; j++) System.out.print(" -"+parameters[j]);
                System.out.println("\n"+instructionsHashMap.get(line[0])+"\n===========================");*/
                programLines[i] = new programLine(instructionsHashMap.get(line[0]), parameters);
            }
            constructor(programLines);
        }

        public BareBonesRuntime(programLine[] lines){
            constructor(lines);
        }

        private void constructor(programLine[] lines) {
            comparators.values();
            programLines = lines;
        }

        public void runCode(){
            while(programCounter<programLines.length){
                programLines[programCounter].runLine(this);
                programCounter++;
            }
            FinalVarsNames = new String[CurrentVars.size()];
            FinalVarsNames = CurrentVars.keySet().toArray(FinalVarsNames);
            Integer[] temp  = new Integer[CurrentVars.size()];
            temp = CurrentVars.values().toArray(temp);
            FinalVarsValues = new int[temp.length];
            for(int i = 0; i < temp.length; i++) FinalVarsValues[i] = temp[i];
        }

        public String[] getVariablesNames(){
            return FinalVarsNames;
        }
        public int[] getVariablesValues(){
            return FinalVarsValues;
        }

        private interface instruction {
            void action(BareBonesRuntime runtime, String[] parameters);
        }
        private interface comparator {
            boolean compare(int x, int y);
        }

        //use of enums to define language behavior
        private enum comparators{
            NOT("not", (x,y) -> x!=y),
            IS("is", (x,y) -> x==y),
            GREATER("greater", (x,y) -> x>y),
            LESSER("lesser", (x,y) -> x<y)

            ;
            private String name;
            private comparator action;
            comparators(String name,comparator action){
                this.name = name;
                this.action = action;
                comparatorsHashMap.put(name, this);
            }
            boolean compare(int x, int y){
                return action.compare(x,y);
            }

        }

        private enum instructions{

            INCREMENT("incr", (runtime, parameters) -> {
                runtime.CurrentVars.replace(parameters[0], runtime.CurrentVars.get(parameters[0])+1);
            }),

            DECREMENT("decr", (runtime, parameters) -> {
                runtime.CurrentVars.replace(parameters[0], runtime.CurrentVars.get(parameters[0])-1);
            }),

            CLEAR("clear", (runtime, parameters) -> {
                if(runtime.CurrentVars.containsKey(parameters[0]))
                    runtime.CurrentVars.replace(parameters[0], 0);
                else
                    runtime.CurrentVars.put(parameters[0], 0);
            }),

            WHILE("while", (runtime, parameters) -> runtime.conditionalJumps.push( new conditionalJump(runtime.programCounter, () -> comparatorsHashMap.get(parameters[1]).compare(runtime.CurrentVars.get(parameters[0]), Integer.parseInt(parameters[2]))))),

            END("end", (runtime, parameters) -> {
                if(runtime.conditionalJumps.peek().evaluate())
                    runtime.programCounter = runtime.conditionalJumps.peek().getLineNumber();
                else
                    runtime.conditionalJumps.pop();
            }),

            // new instructions are now easily added
            // this is done by creating a anonymous instance of the interface instruction
            /*
            Lets say you want to add a instruction that squares the number held in the variable
            it would look like:
            square X

            to create it would wold just write here :
            SQUARE("square", (runtime, parameters) -> runtime.CurrentVars.replace(parameters[0], math.pow(2,runtime.CurrentVars.get(parameters[0])))),

            and that would add the instruction to the language
             */
            ;

            private String name;
            private instruction action;
            instructions(String name,instruction action){
                this.name = name;
                this.action = action;
                instructionsHashMap.put(name, this);
            }
            public void runAction(BareBonesRuntime runtime, String[] parameters){
                /*System.out.println("Running : " + name + " With parameters : ");
                for(String temp : parameters) System.out.println(" -"+temp);*/
                action.action(runtime, parameters);
            }
        }

    }

    public static BareBonesRuntime RunBareBones(String code){
        String[] codeLines = code.split(";");

        for(int i = 0; i< codeLines.length; i++)
            codeLines[i] = codeLines[i].trim();

        BareBonesRuntime runtime = new BareBonesRuntime(codeLines);
        runtime.runCode();
        return runtime;
    }

}
