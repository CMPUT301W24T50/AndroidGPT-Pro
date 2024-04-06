package com.example.androidgpt_pro;

import java.util.ArrayList;

public class Pagination {
    private final int itemsPerPage, lastPageItems, lastPage;
    private final ArrayList<Event> eventData;

    public Pagination(int itemsPerPage, ArrayList<Event> eventData) {
        this.itemsPerPage = itemsPerPage;
        this.eventData = eventData;
        int totalItem = eventData.size();
        this.lastPage = totalItem / itemsPerPage;
        this.lastPageItems = totalItem % itemsPerPage;
    }

    public ArrayList<Event> generateData(int currentPage) {
        int startItem = currentPage * itemsPerPage;
        ArrayList<Event> newPageData = new ArrayList<>();
        if(currentPage == lastPage) {
            // collecting data separately for the last page
            for(int count = 0; count < itemsPerPage; count++) {
                newPageData.add(eventData.get(startItem + count));
            }
        }
        else {
            // for all other pages
            for(int count = 0; count < itemsPerPage; count++) {
                newPageData.add(eventData.get(startItem + count));
            }
        }
        return newPageData;
    }

    /**
     * this is the getter of last page of the Pagination
     * @return lastPage
     */
    public int getLastPage() {
        return lastPage;
    }
}
