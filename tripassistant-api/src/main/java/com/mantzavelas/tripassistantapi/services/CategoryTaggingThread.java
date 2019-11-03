package com.mantzavelas.tripassistantapi.services;

import com.mantzavelas.tripassistantapi.models.PhotoCategory;
import com.mantzavelas.tripassistantapi.models.PhotoCategoryEnum;
import com.mantzavelas.tripassistantapi.photos.datamuse.DataMuseRestClient;
import com.mantzavelas.tripassistantapi.photos.datamuse.RelativeWord;
import com.mantzavelas.tripassistantapi.repositories.PhotoCategoryRepository;
import com.mantzavelas.tripassistantapi.utils.BeanUtil;
import com.mantzavelas.tripassistantapi.utils.DateUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryTaggingThread implements Runnable{

    private static int TAGGING_THREAD_INTERVAL = DateUtils.hoursToMs(12);

    private PhotoCategoryRepository repository;

    private DataMuseRestClient restClient;

    private TaggingService taggingService;

    public CategoryTaggingThread() {
        repository = BeanUtil.getBean(PhotoCategoryRepository.class);
        restClient = DataMuseRestClient.create();
        taggingService = new TaggingService();
    }

	public CategoryTaggingThread(PhotoCategoryRepository repository, DataMuseRestClient restClient, TaggingService taggingService) {
		this.repository = repository;
		this.restClient = restClient;
		this.taggingService = taggingService;
	}

	@Override
    public void run() {
    	while (true) {
			persistCategoriesWithTags();

			taggingService.categorizePhotos();

			try {
				Thread.sleep(TAGGING_THREAD_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }

    private void persistCategoriesWithTags() {
        for (PhotoCategoryEnum categoryEnum :PhotoCategoryEnum.values()) {
            if (repository.existsByCategory(categoryEnum)) {
                return;
            }
            List<RelativeWord> words = restClient.getRelativeWords(categoryEnum.name().toLowerCase());
            List<String> categoryTags = words.stream()
                .map(RelativeWord::getWord)
                .collect(Collectors.toList());

            addStandardCategoryTags(categoryEnum, categoryTags);

            PhotoCategory category = new PhotoCategory();
            category.setTags(categoryTags);
            category.setCategory(categoryEnum);
            repository.save(category);
        }
    }

    private void addStandardCategoryTags(PhotoCategoryEnum categoryEnum, List<String> categoryTags) {
        switch (categoryEnum) {
            case ACTIVITIES:
                categoryTags.addAll(ACTIVITIES_FB_TAGS);
                break;
            case COFFEE:
                categoryTags.addAll(COFFEE_FB_TAGS);
                break;
            case ENTERTAINMENT:
                categoryTags.addAll(ENTERTAINMENT_FB_TAGS);
                break;
            case FOOD:
                categoryTags.addAll(FOOD_FB_TAGS);
                break;
            case SHOPPING:
                categoryTags.addAll(SHOPPING_FB_TAGS);
                break;
            case SIGHTSEEING:
                categoryTags.addAll(SIGHTSEEING_FB_TAGS);
                break;
            case NIGHTLIFE:
                categoryTags.addAll(NIGHTLIFE_FB_TAGS);
        }
    }

    private static final List<String> FOOD_FB_TAGS = Arrays.asList(
            "Food & Beverage",
            "Bagel Shop",
            "Bakery",
            "Butcher Shop",
            "Caterer",
            "Cheese Shop",
            "Deli",
            "Dessert Shop",
            "Candy Store",
            "Chocolate Shop",
            "Cupcake Shop",
            "Frozen Yogurt Shop",
            "Gelato Shop",
            "Ice Cream Shop",
            "Shaved Ice Shop",
            "Distillery",
            "Donut Shop",
            "Farmers Market",
            "Foodservice Distributor",
            "Food Consultant",
            "Food Delivery Service",
            "Food Stand",
            "Food Truck",
            "Food Wholesaler",
            "Personal Chef",
            "Restaurant",
            "Sandwich Shop",
            "Grocery Store",
            "Cuisine",
            "Nutritionist",
            "Greek Restaurant",
            "Mediterranean Restaurant",
            "Food Stand",
            "Italian Restaurant",
            "Seafood Restaurant",
            "Diner",
            "Food & Beverage Company",
            "Dessert Shop"
    );

    private static final List<String> ENTERTAINMENT_FB_TAGS = Arrays.asList(
            "Art",
            "Books & Magazines",
            "Concert Tour",
            "Media Restoration Service",
            "Show",
            "Music",
            "Theatrical Play",
            "Theatrical Productions",
            "TV & Movies",
            "Arts & Entertainment",
            "Amusement & Theme Park",
            "Water Park",
            "Aquarium",
            "Arcade",
            "Art Gallery",
            "Betting Shop",
            "Bingo Hall",
            "Casino",
            "Circus",
            "Escape Game Room",
            "Haunted House",
            "Karaoke",
            "Movie Theater",
            "Dance & Night Club",
            "Pool & Billiard Hall",
            "Race Track",
            "Salsa Club",
            "Performance & Event Venue",
            "Museum",
            "Zoo",
            "Outdoor Recreation",
            "Event",
            "Just For Fun",
            "Live Music Venue",
            "Musician",
            "Media/News Company",
            "Radio Station",
            "Playground"
    );

    private static final List<String> COFFEE_FB_TAGS = Arrays.asList(
            "Food & Beverage",
            "Bakery",
            "Brewery",
            "Bubble Tea Shop",
            "Cafe",
            "Coffee Shop",
            "Tea Room",
            "Cafeteria",
            "Wine, Beer & Spirits Store",
            "Smoothie & Juice Bar",
            "Bar",
            "Winery/Vineyard",
            "Smoothie & Juice Bar"
    );

    private static final List<String> SHOPPING_FB_TAGS = Arrays.asList(
            "Shopping & Retail",
            "Antique Store",
            "Apparel & Clothing",
            "Arts & Crafts Store",
            "Auction House",
            "Beauty Store",
            "Big Box Retailer",
            "Boutique Store",
            "Collectibles Store",
            "Cultural Gifts Store",
            "Department Store",
            "Discount Store",
            "Duty-Free Shop",
            "Movie & Music Store",
            "E-Cigarette Store",
            "Educational Supply Store",
            "Electronics Store",
            "Fabric Store",
            "Fireworks Retailer",
            "Flea Market",
            "Gift Shop",
            "Glass & Mirror Shop",
            "Gun Store",
            "Hobby Store",
            "Home & Garden Store",
            "Lottery Retailer",
            "Mattress Wholesaler",
            "Mobile Phone Shop",
            "Musical Instrument Store",
            "Newsstand",
            "Night Market",
            "Office Equipment Store",
            "Outlet Store",
            "Moving Supply Store",
            "Party Supply & Rental Shop",
            "Pawn Shop",
            "Pet Store",
            "Pop-Up Shop",
            "Rent to Own Store",
            "Restaurant Supply Store",
            "Retail Company",
            "Seasonal Store",
            "Shopping Mall",
            "Shopping Service",
            "Souvenir Shop",
            "Sporting Goods Store",
            "Outdoor & Sporting Goods Company",
            "Thrift & Consignment Store",
            "Tobacco Store",
            "Bookstore",
            "Toy Store",
            "Trophies & Engraving Shop",
            "Video Game Store",
            "Vintage Store",
            "Wholesale & Supply Store",
            "Shopping District",
            "Brand",
            "Local Business",
            "Homebrew Supply Store",
            "Women's Clothing Store",
            "Footwear Store",
            "Shopping & Retail",
            "Specialty Grocery Store"
    );

    private static final List<String> SIGHTSEEING_FB_TAGS = Arrays.asList(
            "Government Building",
            "Capitol Building",
            "City Hall",
            "Consulate & Embassy",
            "Courthouse",
            "Fire Station",
            "Military Base",
            "Police Station",
            "Town Hall",
            "Locality",
            "Congressional District",
            "City",
            "Designated Market Area",
            "Harbor",
            "Large Geo Area",
            "Medium Geo Area",
            "Metro Area",
            "Neighborhood",
            "Port",
            "Shopping District",
            "Religious Place of Worship",
            "Landmark & Historical Place",
            "Monument",
            "Castle",
            "Travel & Transportation",
            "Statue & Fountain",
            "Art Museum",
            "Sculpture Garden"
    );

    private static final List<String> ACTIVITIES_FB_TAGS = Arrays.asList(
            "Interest",
            "Literary Arts",
            "Performance Art",
            "Performing Arts",
            "Science",
            "Sports",
            "Sports Club",
            "Social Club",
            "Visual Arts",
            "Sorority & Fraternity",
            "Youth Organization",
            "Charity Organization",
            "Community Organization",
            "Professional Service",
            "Convention Center",
            "Hotel Services Company",
            "Outdoor Recreation",
            "Event Space",
            "Library",
            "Event",
            "Hotel",
            "Playground"
    );

    private static final List<String> NIGHTLIFE_FB_TAGS = Arrays.asList(
            "Wine Bar",
            "Dance & Night Club",
            "Musician",
            "Homebrew Supply Store",
            "Pub",
            "Dance & Night Club",
            "Bar"
    );

}
