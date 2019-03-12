import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class main {
    public static void main(String[] arc) {


        String path = "/Users/kevin/Desktop/New Classes/Programming Techniques/Robson/in.txt";
        BTNode root = create(path);
        preOrderTraversal(root);
        modRobson(root);
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
        BTNode stack;
        BTNode avail;

        while (true) {
            printNode(p);
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
                while (!exchanged && predp.info != -1){// || predp.info == root.info)) {
                    //System.out.println("inside while");
                    BTNode old = p;
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
                    } else if (top != null && predp.info == top.right.info) {// This is saying neither left nor right subtree are null.
                                                                             // And also top isn't null AND top's right subtree is predp.
                                                                             // I believe this means we are coming up from a right subtree.
                        //System.out.println("Top does not equal null condition. Predp is " + predp.info + ", and p is " + p.info);
                        boolean flag = false;
                        if (p.info == 8){
                            flag = true;
                        //    System.out.println("R: Root left is " + root.left + " and root right is " + root.right.info);
                        }
                        BTNode old_top = top; // Temp top variable.
                        BTNode new_predp = predp.left; // Left pointer actually points to predecessor.
                        predp.left = predp.right; // predp's right pointer actually points to its left successor.
                        predp.right = p; // Restore predp's right link.
                        p = predp; // Move up a level.
                        predp = new_predp; // Move up predp.
                        top = top.left; // top's left subtree must point to its predecessor also.
                        old_top.left = null; // This is also modifying what used to be top.
                        old_top.right = null; // ^
                        if (flag){
                            flag = false;
                        //    System.out.println("Now p is " + p.info + "and predp is " + predp.info);
                        }
                    } else {
                        // In this case, we are coming up from the left, and we have not yet traversed the right subtree.
                        //System.out.println("Coming up from the left, p is " + p.info + " and Predp is " + predp.info);
                        if (p.info == 2){
                        //    System.out.println("L: Root left is " + root.left + " and root right is " + root.right.info);
                        }
                        BTNode newP = null; // Trying to find a new p.
                        if (avail != null) { // We must have some sort of available pointer to become the next stack.
                            newP = predp.right; // predp's right subtree points to the right subtree.
                            avail.left = top; // avail is a new stack entry.
                            avail.right = predp;
                            top = avail; // And top of the stack.
                            avail = null; // it is not longer available.
                            predp.right = p; // predp's right pointer now points to its left successor because it's left pointer points to its pred.
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
            //try{ Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace();}
        }
    }










/*
                    // This indicates we're at a leaf node, and must backtrack.
                    // If predp == top, then we just finished traversing a right subtree.
                    // Otherwise, we finished traversing a left subtree.
                    //If we're coming up from the right, we can keep going until there is a new right node.
                    if (predp.info == top.info) {
                        do {
                            //Restore the tree.
                            predp.right = p;
                            p = predp;
                        } while (false);
                    } else {
                        predp.left = p; // Restore predp's left connection.
                        p = predp; // This will take us up one level.
                        if (p.right != null) { // if p.right isn't null, that means we're traversing the right subtree that has a root with non-null left subtreee.
                            
                        }
                    }
                }

            } else{
                break;
            }
        }


        Stack list = new Stack();
        while (p != null || list.length() > 0){
            if (p != null) {
                list.push(p);
                printNode(p);
                System.out.println();
                if (p.left != null){
                    p = p.left;
                } else{
                    p = p.right;
                }
            } else{
                BTNode rptr = null;
                BTNode q;
                do{
                    q = list.pop();
                    //System.out.println("Inside do/while. q is " + q.info);
                    if (list.length() > 0){
                        rptr = list.pop().right;
                    } //rptr is null by default, don't need an else.
                }while(list.length() > 0 && q.equals(rptr));
                p = rptr;
            }
        }*/

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
}