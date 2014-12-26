package client;

import javax.swing.AbstractListModel;

public class UIRecordModel extends AbstractListModel {

    @Override
    public int getSize() {
        if (Client.currentRecords == null) return 0;
        else return Client.currentRecords.size();
    }

    @Override
    public Object getElementAt(int index) {
        return Client.currentRecords.get(index);
    }

    public void refresh() {
        String name = Client.currentTargetName;
        if (Client.currentChatType == Client.GROUP) {
            Client.currentRecords = Client.roomChatRecords.get(name);
        } else if(Client.currentChatType == Client.USER) {
            Client.currentRecords = Client.userChatRecords.get(name);
        }
        if (Client.currentRecords == null) return;
        this.fireContentsChanged(this, 0, Client.currentRecords.size() - 1);
    }

    public void update() {
        int size = Client.currentRecords.size();
        this.fireContentsChanged(this, size - 2, size - 1);
    }
}
