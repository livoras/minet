package client;

import javax.swing.AbstractListModel;

import org.json.JSONException;

public class UIUserModel extends AbstractListModel {
    @Override
    public int getSize() {
        return Client.users.length();
    }

    @Override
    public Object getElementAt(int index) {
        try {
            return Client.users.get(index);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return index;
    }

    public void update() {
        this.fireContentsChanged(this, 0, Client.users.length() - 1);
    }
}


