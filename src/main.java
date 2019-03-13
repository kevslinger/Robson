import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class main {
    public static void main(String[] arc) {
        
        String path = "in.txt";
        BTNode root = create(path);
        preOrderTraversal(root);
        System.out.println();
        modRobson(root);
        System.out.println();
        preOrderTraversal(root);

    }


    public static BTNode create(String path) {
        //IO Declarations.
        File f = new File(path);
        BufferedReader buff = null;

        BTNode root = null;//This is our binary tree.
        Stack list = new Stack();
        int i = 1; //Keeps track of what Node we are on.
        try {
            buff = new BufferedReader(new FileReader(f));
        } catch (IOException e) {
            System.out.println("Could not read file.");
        }
        try {
            int isNull = Integer.parseInt(buff.readLine());
            if (isNull == 0) {
                //Tree is null.
                return null;
            } else {//Root is the first line after 0/1 null tree determination.
                String line = buff.readLine();
                root = new BTNode(i++, line);//Increment i after initializing it.
                //Always push right first because we want to visit left (as per pre-order traversal).
                if (root.right != null){
                    list.push(root.right);
                }
                if (root.left != null) {
                    list.push(root.left);
                }//Loop for all remaining lines in data.
                while ((line = buff.readLine()) != null) {
                    //Note that the number of lines matches the number of nodes in the tree.
                    //System.out.println(i);
                    BTNode n = list.pop();
                    n.info = i++;
                    String[] tok = line.split(" ");
                    int left = Integer.parseInt(tok[0]);
                    int right = Integer.parseInt(tok[1]);
                    if (right == 0){
                        n.right = null;
                    } else{
                        n.right = new BTNode();
                        list.push(n.right);
                    }
                    if (left == 0){
                        n.left = null;
                    } else{
                        n.left = new BTNode();
                        list.push(n.left);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("IOError");
        }
        return root;
    }

    /**
     * Function to print tree in pre-order traversal.
     * Code was taken from our class note pseudo-code.
     * @param root
     */
    public static void preOrderTraversal(BTNode root){
        BTNode p = root;
        Stack list = new Stack();
        while (p != null || list.length() > 0){
            if (p != null) {
                printNode(p);
                list.push(p);
                if (p.left != null){
                    p = p.left;
                } else{
                    p = p.right;
                }
            } else{
                BTNode rptr;
                BTNode q;
                do{
                    q = list.pop();
                    if (list.length() > 0){
                        rptr = list.peek().right;
                    } else {
                        rptr = null;
                    }
                }while(list.length() > 0 && q.equals(rptr));
                p = rptr;
            }
        }
    }


    //Preorder traversal with Robson's algorithm
    public static void modRobson(BTNode root) {
        BTNode p = root;
        BTNode top = null;//We can never have a negative node, so this makes for a good sentinel.
        BTNode predp = new BTNode(-1);
        BTNode stack = null;
        BTNode avail;

        while (true) {
            printNode(p);
            printMetadata(p, predp, stack, root);
            //System.out.println(root.right.info);
            if (p.left != null) {//We have at least a left child.
                BTNode tmp = p.left;
                p.left = predp;
                predp = p;
                p = tmp;
            } else if (p.right != null) {//No left child, but we have a right child.
                BTNode tmp = p.right;
                p.right = predp;
                predp = p;
                p = tmp;
            } else {//Neither left nor right child.
                boolean exchanged = false;
                avail = p;
                while (!exchanged && predp.info != -1){
                    if (predp.right == null) {// This means we're in a left subtree with a null right subtree.
                        BTNode new_predp = predp.left; // Predp's left is its predecessor.
                        predp.left = p;// Restore the link to predp's left subtree.
                        p = predp;// Move up to predp.
                        predp = new_predp;// Move predp up to new_predp.
                    } else if (predp.left == null) {// if the right subtree is NOT null, but the left subtree IS null
                        BTNode new_predp = predp.right;// Then similar to be done with moving back up the tree.
                        predp.right = p;
                        p = predp;
                        predp = new_predp;
                    } else if (top != null && predp.info == top.info) {// This is saying neither left nor right subtree are null.
                                                                             // And also top isn't null AND top's right subtree is predp.
                                                                             // this means we are coming up from a right subtree.
                        BTNode oldStack = stack;
                        stack = stack.left;
                        BTNode new_predp = predp.left; // Left pointer actually points to predecessor.
                        predp.left = predp.right; // predp's right pointer actually points to its left successor.
                        predp.right = p; // Restore predp's right link.
                        p = predp; // Move up a level.
                        predp = new_predp; // Move up predp.
                        top = oldStack.right; // top's left subtree must point to its predecessor also.
                        oldStack.left = null; // This is also modifying what used to be top.
                        oldStack.right = null; // ^
                    } else {
                        // In this case, we are coming up from the left, and we have not yet traversed the right subtree.
                        BTNode newP = null; // Trying to find a new p.
                        if (avail != null) { // We must have some sort of available pointer to become the next stack.
                            BTNode oldStack = stack;
                            stack = avail;
                            avail = null;
                            newP = predp.right; // predp's right subtree points to the right subtree.
                            predp.right = p; // predp's right pointer now points to its left successor because it's left pointer points to its pred.
                            stack.left = oldStack; // avail is a new stack entry.
                            stack.right = top;

                            top = predp;

                            p = newP; // move p over to the right subtree.
                            exchanged = true; // We did the exchange!
                        } else {
                            System.out.println("Avail error");
                        }
                    }
                }
                if (!exchanged) {
                    return;
                }
            }
        }
    }



    public static void printNode(BTNode node){
        if (node == null){
            return;
        }
        System.out.print("Value of node is " + node.info + ". ");
        if (node.left != null){
            System.out.print("Value of node's left successor is " + node.left.info + ". ");
        } else {
            System.out.print("Left successor is null. ");
        }
        if (node.right != null){
            System.out.print("Value of node's right successor is " + node.right.info + ". ");
        } else {
            System.out.print("Right successor is null. ");
        }
        System.out.println();
    }

    public static void printMetadata(BTNode p, BTNode predp, BTNode stack, BTNode root){
        if (p.info == 1){
            System.out.println();
            return;
        }
        System.out.println("Printing Path from " + p.info + " to root...");
        BTNode tempPredp = predp;
        while(tempPredp.info != 1){
            printNode(tempPredp);
            if (tempPredp.left != null && tempPredp.left.info < tempPredp.info){
                tempPredp = tempPredp.left;
            } else if (tempPredp.right != null && tempPredp.right.info < tempPredp.info){
                tempPredp = tempPredp.right;
            } else {
                System.exit(9);
            }
        }
        printNode(tempPredp);

        System.out.println("Printing out stack...");
        BTNode tempStack = stack;
        while(tempStack != null){
            printNode(tempStack);
            tempStack = tempStack.left;
        }

        System.out.println();
    }
}