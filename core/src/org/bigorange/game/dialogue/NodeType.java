package org.bigorange.game.dialogue;

import java.util.HashMap;
import java.util.Map;

public enum NodeType {
    START(1),
    END(2),
    MESSAGE(3),
    CHOICE(4),
    UNDEFINED(5);

    private int nodeTypeCode;

    private static Map<Integer, NodeType>  mapper = new HashMap<>();

    static {
        for (NodeType nodeType : NodeType.values()) {
            mapper.put(nodeType.nodeTypeCode, nodeType);
        }
    }

    NodeType(int nodeTypeCode) {
        this.nodeTypeCode = nodeTypeCode;
    }

    public int getNodeTypeCode(){
        return nodeTypeCode;
    }
    public static NodeType getNodeTypeByCode(int nodeTypeCode){
        return  mapper.get(nodeTypeCode);
    }
}
