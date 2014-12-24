package edu.uga.dawgtrades.ui;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import edu.uga.dawgtrades.DTException;
import edu.uga.dawgtrades.control.*;
import edu.uga.dawgtrades.ui.*;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import edu.uga.dawgtrades.model.*;
import java.util.*;


/**
 *
 * @author reanimus
 */
public class SearchUI extends HttpServlet {

    // Gotta love floating point
    private boolean epsilonEquals(double x, double y) {
        double epsilon = Math.max(Math.ulp(x), Math.ulp(y));
        return Math.abs(x-y) < epsilon;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get current session
        HttpSession session = request.getSession(true);
        request.setAttribute("baseContext", session.getServletContext().getContextPath());
        LoginControl ctrl = new LoginControl();
        if(ctrl.checkIsLoggedIn(session))
        {
            RegisteredUser currentUser = (RegisteredUser) session.getAttribute("currentSessionUser");
            request.setAttribute("loggedInUser", currentUser);
        }else{
            request.setAttribute("loggedInUser", "");;
            request.removeAttribute("loggedInUser");
        }

        CategoryControl catCtrl = new CategoryControl();

        // If this is present, on step 2
        String chooseCategoryToSearch = request.getParameter("category");

        // If this is present, on step 3
        String searchCategory = request.getParameter("searchCategory");

        // If nether, on step one.
        if(chooseCategoryToSearch != null) {
            try {
                // Step 2: enter attribute data

                // Grab category.
                long id = Long.parseLong(chooseCategoryToSearch, 10);
                Category toSearch = catCtrl.getCategoryWithID(id);
                if(toSearch != null) {
                    request.setAttribute("searchCategory", toSearch);

                    // Now, get the list of attributeTypes for it.
                    AuctionControl auctionCtrl = new AuctionControl();
                    ArrayList<AttributeType> attributeTypes;
                    if(id != 0) {
                        attributeTypes = auctionCtrl.getAttributeTypesForCategory(id);
                        if(attributeTypes == null) {
                            if(catCtrl.hasError()) {
                                request.setAttribute("error", "Error fetching attributes: " + catCtrl.getError());
                                request.setAttribute("returnTo", "/");
                                request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            }else{
                                request.setAttribute("error", "Internal error while fetching attributes.");
                                request.setAttribute("returnTo", "/");
                                request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            }
                        }
                    }else{
                        attributeTypes = new ArrayList<AttributeType>();
                    }
                    request.setAttribute("attributeTypes", attributeTypes);
                    request.getRequestDispatcher("/searchCategory.ftl").forward(request, response);
                    return;
                }else{
                    if(catCtrl.hasError()) {
                        request.setAttribute("error", catCtrl.getError());
                        request.setAttribute("returnTo", "/search");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                    }else{
                        request.setAttribute("error", "Category does not exist.");
                        request.setAttribute("returnTo", "/search");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                    }
                }
            }
            catch(NumberFormatException e) {
                request.setAttribute("error", "Invalid category ID. Please try again.");
                request.setAttribute("returnTo", "/search");
                request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                return;
            }

        }else if(searchCategory != null){
            // On step 3: do the search.
            // This is meaty, follow carefully.
            try {

                // Grab the category.
                long id = Long.parseLong(searchCategory, 10);
                Category toSearch = catCtrl.getCategoryWithID(id);
                if(toSearch != null) {
                    request.setAttribute("searchCategory", toSearch);

                    // Grab the list of attributetypes.
                    AuctionControl auctionCtrl = new AuctionControl();
                    ArrayList<AttributeType> attributeTypes;
                    if(id != 0) {
                        attributeTypes = auctionCtrl.getAttributeTypesForCategory(id);
                        if(attributeTypes == null) {
                            if(catCtrl.hasError()) {
                                request.setAttribute("error", "Error fetching attributes: " + catCtrl.getError());
                                request.setAttribute("returnTo", "/");
                                request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            }else{
                                request.setAttribute("error", "Internal error while fetching attributes.");
                                request.setAttribute("returnTo", "/");
                                request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            }
                        }
                    }else{
                        attributeTypes = new ArrayList<AttributeType>();
                    }
                    request.setAttribute("attributeTypes", attributeTypes);

                    // Now attempt to do search query. Start by getting all open auctions in the category.
                    ArrayList<Auction> auctions = catCtrl.getCategoryAuctions(id);
                    if(auctions == null) {
                        if(catCtrl.hasError()) {
                            request.setAttribute("error", catCtrl.getError());
                            request.setAttribute("returnTo", "/search");
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        }else{
                            request.setAttribute("error", "Failed to get auctions.");
                            request.setAttribute("returnTo", "/search");
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        }
                    }

                    // Now, grab the items
                    HashMap<String, Item> itemsForAuctions = catCtrl.getItemsForAuctions(auctions);

                    if(itemsForAuctions == null) {
                        if(catCtrl.hasError()) {
                            request.setAttribute("error", catCtrl.getError());
                            request.setAttribute("returnTo", "/search");
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        }else{
                            request.setAttribute("error", "Failed to get items.");
                            request.setAttribute("returnTo", "/search");
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        }
                    }

                    request.setAttribute("itemsForAuctions", itemsForAuctions);

                    // Now, apply constraints on name + description (universal)
                    String findName = request.getParameter("name");
                    if(findName != null) {
                        request.setAttribute("currentName", findName);
                    }
                    String findDescription = request.getParameter("description");
                    if(findDescription != null) {
                        request.setAttribute("currentDescription", findDescription);
                    }
                    ArrayList<Auction> candidates = new ArrayList<Auction>();
                    for(Auction auction : auctions) {
                        Item item = itemsForAuctions.get(Long.valueOf(auction.getId()).toString());
                        if(findName != null && !findName.isEmpty()) {
                            findName = findName.toLowerCase();
                            if(!item.getName().toLowerCase().contains(findName)) {
                                continue;
                            }
                        }
                        if(findDescription != null && !findDescription.isEmpty()) {
                            findDescription = findDescription.toLowerCase();
                            if(!item.getDescription().toLowerCase().contains(findDescription)) {
                                continue;
                            }
                        }
                        // Made it, add it to candidates.
                        candidates.add(auction);
                    }

                    SearchControl searchCtrl = new SearchControl();

                    // Next, attributetypes...

                    // Store the current values in a map for the page to use
                    HashMap<String, String> searchVals = new HashMap<String, String>();

                    for(AttributeType attribute : attributeTypes) {
                        // Store current matches for this attribute
                        ArrayList<Auction> currentCandidates = new ArrayList<Auction>();

                        // Grab form value
                        String attrValString = request.getParameter("attr_" + Long.valueOf(attribute.getId()).toString());

                        // If empty, skip
                        if(attrValString == null || attrValString.isEmpty()) {
                            continue;
                        }else{
                            // Not empty. Get value and put it in storage.
                            searchVals.put("attr_" + Long.valueOf(attribute.getId()).toString(), attrValString);

                            // If it's a string attribute...
                            if(attribute.getIsString()) {

                                // Convert to lowercase.
                                attrValString = attrValString.toLowerCase();
                                for(Auction auction : candidates) {
                                    // Grab item for auction.
                                    Item item = itemsForAuctions.get(Long.valueOf(auction.getId()).toString());

                                    // Get the value of its attribute
                                    String itemValueString = searchCtrl.getAttributeWithTypeForItem(attribute, item);
                                    if(itemValueString == null) {
                                        // Assuming we got nothing...
                                        if(searchCtrl.hasError()) {
                                            request.setAttribute("error", "Error parsing backend data: " + searchCtrl.getError());
                                            request.setAttribute("returnTo", "/search");
                                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                                            return;
                                        }else{
                                            continue;
                                        }
                                    }

                                    // Convert to lower
                                    itemValueString = itemValueString.toLowerCase();

                                    // Check if it's inside. If so, it's a match.
                                    if(itemValueString.contains(attrValString)) {
                                        currentCandidates.add(auction);
                                    }
                                }
                            }else{
                                // Numbers. Parse the search number.
                                double attrVal = 0;
                                try {
                                    attrVal = Double.valueOf(attrValString);
                                }
                                catch(NumberFormatException e) {
                                    request.setAttribute("error", "You passed in an invalid value for " + attribute.getName() + " (not a number).");
                                    request.setAttribute("returnTo", "/search?category=" + searchCategory);
                                    request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                                }

                                // Get the comparison type.
                                String attrValComp = request.getParameter("attr_" + Long.valueOf(attribute.getId()).toString() + "_comparison");
                                if(
                                    attrValComp == null ||
                                    !(
                                        attrValComp.equals("lt") ||
                                        attrValComp.equals("lte") ||
                                        attrValComp.equals("eq") ||
                                        attrValComp.equals("neq") ||
                                        attrValComp.equals("gte") ||
                                        attrValComp.equals("gt")
                                    )
                                ) {
                                    // Got a search key without valid comparison operator, skip it.
                                    continue;
                                }

                                // Store search type.
                                searchVals.put("attr_" + Long.valueOf(attribute.getId()).toString() + "_comparison", attrValComp);

                                for(Auction auction : candidates) {
                                    // Grab item for auction.
                                    Item item = itemsForAuctions.get(Long.valueOf(auction.getId()).toString());

                                    // Grab attribute value.
                                    String itemValueString = searchCtrl.getAttributeWithTypeForItem(attribute, item);
                                    if(itemValueString == null) {
                                        if(searchCtrl.hasError()) {
                                            request.setAttribute("error", "Error parsing backend data: " + searchCtrl.getError());
                                            request.setAttribute("returnTo", "/search");
                                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                                            return;
                                        }else{
                                            continue;
                                        }
                                    }

                                    // Convert to double
                                    double itemVal = 0;
                                    try {
                                        itemVal = Double.valueOf(itemValueString);
                                    }
                                    catch(NumberFormatException e) {
                                        request.setAttribute("error", "Error parsing backend value for " + attribute.getName() + " (not a number).");
                                        request.setAttribute("returnTo", "/search?category=" + searchCategory);
                                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                                    }

                                    // Do comparison.
                                    switch(attrValComp) {
                                        case "lt":
                                            if(itemVal < attrVal) {
                                                currentCandidates.add(auction);
                                            }
                                            break;
                                        case "lte":
                                            if(itemVal <= attrVal) {
                                                currentCandidates.add(auction);
                                            }
                                            break;
                                        case "eq":
                                            if(this.epsilonEquals(itemVal,attrVal)) {
                                                currentCandidates.add(auction);
                                            }
                                            break;
                                        case "neq":
                                            if(!this.epsilonEquals(itemVal,attrVal)) {
                                                currentCandidates.add(auction);
                                            }
                                            break;
                                        case "gte":
                                            if(itemVal >= attrVal) {
                                                currentCandidates.add(auction);
                                            }
                                            break;
                                        case "gt":
                                            if(itemVal > attrVal) {
                                                currentCandidates.add(auction);
                                            }
                                            break;
                                        default:
                                            // This never happens thanks to the guard above
                                    }
                                }
                            }

                            // After removing all non-matches, candidates is set for the next iteration.
                            candidates = currentCandidates;
                        }
                    }

                    // Save values
                    request.setAttribute("currentValues", searchVals);

                    // candidates should now contain search results.
                    request.setAttribute("searchResults", candidates);

                    // Get bids
                    HashMap<String, Bid> bids = catCtrl.getBidsForAuctions(candidates);
                    if(bids == null) {
                        if(catCtrl.hasError()) {
                            request.setAttribute("error", "Error fetching bids: " + catCtrl.getError());
                            request.setAttribute("returnTo", "/search");
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            return;
                        }else{
                            request.setAttribute("error", "Internal error fetching bids.");
                            request.setAttribute("returnTo", "/search");
                            request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                            return;
                        }
                    }
                    request.setAttribute("auctionBids", bids);

                    // Render.
                    request.getRequestDispatcher("/searchResults.ftl").forward(request, response);
                    return;
                }else{
                    if(catCtrl.hasError()) {
                        request.setAttribute("error", catCtrl.getError());
                        request.setAttribute("returnTo", "/search");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;
                    }else{
                        request.setAttribute("error", "Category does not exist.");
                        request.setAttribute("returnTo", "/search");
                        request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                        return;
                    }
                }
            }
            catch(NumberFormatException e) {
                request.setAttribute("error", "Invalid category ID. Please try again.");
                request.setAttribute("returnTo", "/search");
                request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                return;

            }
        }else{

            // Step 1. Choose category.
            HashMap<String, ArrayList> children = catCtrl.populateHashmapWithCategories(0);
            if(children != null) {
                request.setAttribute("categoriesMap", children);
                request.getRequestDispatcher("/searchChooseCategory.ftl").forward(request, response);
                return;
            }else{
                if(catCtrl.hasError()) {
                    request.setAttribute("error", "Error getting categories: " + catCtrl.getError());
                    request.setAttribute("returnTo", "/");
                    request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                    return;
                }else{
                    request.setAttribute("error", "Internal error getting categories.");
                    request.setAttribute("returnTo", "/");
                    request.getRequestDispatcher("/genericError.ftl").forward(request, response);
                    return;
                }

            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Search UI";
    }// </editor-fold>

}
