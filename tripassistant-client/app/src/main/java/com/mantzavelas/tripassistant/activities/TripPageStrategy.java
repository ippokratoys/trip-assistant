package com.mantzavelas.tripassistant.activities;

class TripPageStrategy {

    private TripFragmentPage fragmentPage;

    public TripPageStrategy(TripFragmentPage fragmentPage) {
        this.fragmentPage = fragmentPage;
    }

    public TripFragmentPage getFragmentPage() { return fragmentPage; }
    public void setFragmentPage(TripFragmentPage fragmentPage) { this.fragmentPage = fragmentPage; }

    public boolean matchesStrategy(String name) {
        return fragmentPage.getTitle().equals(name);
    }
}
