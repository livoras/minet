package client;

import javax.swing.AbstractListModel;

import org.json.JSONException;

public class UIRoomModel extends AbstractListModel {
    @Override
    public int getSize() {
        return Client.allRooms.length();
    }

    @Override
    public Object getElementAt(int index) {
        try {
            return Client.allRooms.get(index);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return index;
    }

    public void update() {
        this.fireContentsChanged(this, 0, Client.allRooms.length() - 1);
    }
}


