public class BTNode {
    int info;
    BTNode left;
    BTNode right;
    //Constructor for adding a node with both left and right children.
    //Note that you can add only a left or only a right by making the other null.
    public BTNode(int val, String pair){
        String[] tok = pair.split(" ");
        if (Integer.parseInt(tok[0]) == 0){
            left = null;
        } else{
            left = new BTNode();
        }
        if (Integer.parseInt(tok[1]) == 1){
            right = new BTNode();
        }
        info = val;
    }
    //Constructor when both leaves are null. Maybe this won't get used?
    public BTNode(int val){
        info = val;
        left = null;
        right = null;
    }
    public BTNode(){
        info = 0;
        left = null;
        right = null;
    }


}
