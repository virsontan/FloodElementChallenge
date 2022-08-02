package planitperformance;

import com.microsoft.playwright.*;

import java.util.ArrayList;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FloodElementChallenge {

    public static void main(String[] args) {
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.webkit().launch(new BrowserType.LaunchOptions()
                    .setHeadless(false));
            BrowserContext context = browser.newContext();

            // Open new page
            Page page = context.newPage();

            // Go to https://element-challenge.flood.io/
            page.navigate("https://element-challenge.flood.io/");

            // Click button:has-text("TAKE THE CHALLENGE")
            page.locator("button:has-text(\"TAKE THE CHALLENGE\")").click();


            //CHALLENGE 1//
            //Determine the percentage discount//
            System.out.println("-----------CHALLENGE 1 START: DETERMINE PERCENTAGE DISCOUNT-------------");
            String discount = page.locator("//h2[@data-test-discount='true']").innerText();
            int value = Integer.parseInt(discount.substring(10, discount.indexOf("%")));
            System.out.println("Challenge 1 - Discount value is: " + value);
            //Select the correct answer//
            page.locator("#challenge-1-option-" + value + " input").click();
            page.locator("button:has-text('CHECK')").click();
            assertThat(page.locator("text=Your answer is correct!")).isVisible();
            page.locator("button:has-text('NEXT')").click();
            System.out.println("-----------CHALLENGE 1 END: DETERMINE PERCENTAGE DISCOUNT-------------");


            //CHALLENGE 2//
            //Count all the category product//
            System.out.println("-----------CHALLENGE 2 START: COUNT CATEGORY PRODUCT-------------");
            String product = page.locator("#challenge-2-category").innerText();
            page.click("[aria-label='" + product + "']");
            System.out.println("Challenge 2 - Total number of " + product + " product is: " + page.locator("//a[contains(@href, '/products/')]").count());
            //Select the correct answer//
            page.locator("#challenge-2-option-" + page.locator("//a[contains(@href, '/products/')]").count() + " input").click();
            page.locator("button:has-text('CHECK')").click();
            assertThat(page.locator("text=Your answer is correct!")).isVisible();
            page.locator("button:has-text('NEXT')").click();
            System.out.println("-----------CHALLENGE 1 END: COUNT CATEGORY PRODUCT-------------");


            //CHALLENGE 3//
            //REVEAL THE DEAL, COPY THE CODE//
            System.out.println("-----------CHALLENGE 3 START: CLICK REVEAL THE DEAL, COPY THE CODE, PASTE THE CODE-------------");
            page.locator("button:has-text('Reveal the deal')").click();
            System.out.println("Challenge 3 - Reveal the deal button clicked");
            page.locator("button:has-text('Copy the code')").click();
            System.out.println("Challenge 3 - Copy the code button clicked");
            page.locator("input[type='text']").click();
            //Paste the CODE in the text box//
            page.keyboard().down("Control");
            page.keyboard().press("v");
            page.keyboard().up("Control");
            System.out.println("Challenge 4 - code pasted in the text box");
            page.locator("button:has-text('CHECK')").click();
            assertThat(page.locator("text=Your answer is correct!")).isVisible();
            page.locator("button:has-text('NEXT')").click();
            System.out.println("-----------CHALLENGE 3 END: CLICK REVEAL THE DEAL, COPY THE CODE, PASTE THE CODE-------------");


            //CHALLENGE 4//
            //JUST GO TO THE PRODUCTS//
            System.out.println("-----------CHALLENGE 4 START: GO TO PRODUCTS PAGE-------------");
            String link = page.locator("#challenges-popup p").textContent();
            String linkSubstring = link.substring(30, 38);
            System.out.println(linkSubstring);
            page.locator("a[role='button']:has-text('" + linkSubstring + "')").click();
            System.out.println("Challenge 4 - Product has been clicked");
            assertThat(page).hasURL("https://element-challenge.flood.io/" + linkSubstring.toLowerCase());
            System.out.println("Challenge 4 - landed in Product page successfully");
            //Validate answer//
            page.locator("button:has-text('CHECK')").click();
            assertThat(page.locator("text=Your answer is correct!")).isVisible();
            page.locator("button:has-text('NEXT')").click();
            System.out.println("-----------CHALLENGE 4 END: GO TO PRODUCTS PAGE-------------");


            //CHALLENGE 5 - ADJUST PRICE RANGE - COUNT THE PRODUCTS//
            System.out.println("-----------CHALLENGE 5 START: ADJUST PRICE RANGE - COUNT THE PRODUCTS-------------");
            int minPriceToMatch = Integer.parseInt(page.locator("#challenge-5-min-price").innerText().substring(1));
            System.out.println("Challenge 5 - minimum price to match is: " + minPriceToMatch);
            int maxPriceToMatch = Integer.parseInt(page.locator("#challenge-5-max-price").innerText().substring(1));
            System.out.println("Challenge 5 - maximum price to match is: " + maxPriceToMatch);
            String priceToggle = page.locator("//p[@data-test-range='true']").innerText();
            int minPriceToggle = Integer.parseInt(priceToggle.substring(1, priceToggle.indexOf("-") - 1));
            System.out.println("Challenge 5 - initial toggle minimum price is: " + minPriceToggle);
            int maxPriceToggle = Integer.parseInt(priceToggle.substring((priceToggle.lastIndexOf("$")) + 1));
            System.out.println("Challenge 5 - initial toggle maximum price is: " + maxPriceToggle);

            if (minPriceToggle != minPriceToMatch) {
                System.out.println("Challenge 5 - adjusting minimum toggle price started!");
                do {
                    page.locator("span[role='slider']").first().press("ArrowRight");
                    minPriceToggle++;
                } while (minPriceToggle != minPriceToMatch);
            }
            System.out.println("Challenge 5 - adjusting minimum toggle price ended!");
            System.out.println("Challenge 5 - toggle minimum price is now: " + minPriceToggle);

            if (maxPriceToggle != maxPriceToMatch) {
                System.out.println("Challenge 5 - adjusting maximum toggle price started!");
                do {
                    page.locator("span[role='slider']").nth(1).press("ArrowLeft");
                    maxPriceToggle--;
                } while (maxPriceToggle != maxPriceToMatch);
            }
            System.out.println("Challenge 5 - adjusting maximum toggle price ended!");
            System.out.println("Challenge 5 - toggle maximum price is now: " + maxPriceToggle);

            System.out.println("Challenge 5 - checking number of pagination...");
            //Get pagination size
            int pageSize = page.locator("ul.MuiPagination-ul li").allTextContents().size();

            //Check pagination size
            if (pageSize == 2) { //Go here if there's only 1 page
                System.out.println("Challenge 5 - number of pagination is only 1");
                System.out.println("Challenge 5 - counting total number of product...");
                //Check product count
                int numberOfProducts = page.locator("//a[contains(@href, '/products/')]").count();
                System.out.println("Challenge 5: total number of product between minimum price and maximum price is: " + numberOfProducts);
                //Input product count in the text box
                page.locator("input[type='text']").fill(String.valueOf(numberOfProducts));
            } else { //Go here if there's more than 1 page
                System.out.println("Challenge 5 - checking number of pagination...");
                System.out.println("Challenge 5 - number of pagination is: " + (pageSize-2));
                System.out.println("Challenge 5 - counting total number of product...");
                //Check product count of 1st page
                int numOfProducts = page.locator("//a[contains(@href, '/products/')]").count();
                System.out.println("Challenge 5 - number of product in Page 1 is: " + numOfProducts);
                int totalNumOfProductsInLoop = 0;
                for (int i = 1; i < pageSize - 2; i++) { //Go here for the 2nd, 3rd, 4th, 5th, 6th,....nth page
                    page.locator("[aria-label='Go to next page']").click();
                    //Store number of product for the ith page
                    int numberOfProductsInLoop = page.locator("//a[contains(@href, '/products/')]").count();
                    System.out.println("Challenge 5 - number of product in Page: " + (i + 1) + " is: " + numberOfProductsInLoop);
                    //Compute for the number of product for all the pages inside the loop (2nd page .... nth page)
                    totalNumOfProductsInLoop = totalNumOfProductsInLoop + numberOfProductsInLoop;
                }
                //Compute for the total number of product 1st page + pages inside the loop (2nd page .... nth page)
                int totalNumOfProducts = numOfProducts + totalNumOfProductsInLoop;
                System.out.println("Challenge 5: total number of product between minimum price and maximum price is: " + totalNumOfProducts);
                page.locator("input[type='text']").fill(String.valueOf(totalNumOfProducts));
            }
            page.locator("button:has-text('CHECK')").click();
            assertThat(page.locator("text=Your answer is correct!")).isVisible();
            page.locator("button:has-text('NEXT')").click();
            System.out.println("-----------CHALLENGE 5 END: ADJUST PRICE RANGE - COUNT THE PRODUCTS-------------");



            //CHALLENGE 6 ADD ITEMS TO CART//
            System.out.println("-----------CHALLENGE 6 START: ADD ITEMS TO CART-------------");
            //Check Number of Products to be added to cart//
            int Challenge6numberOfProducts = page.locator("//a[contains(@href, '/products/')]").count();
            System.out.println("Challenge 6 - adding product to cart...");
            if (Challenge6numberOfProducts == 0) {
                System.out.println("Challenge 6 - total number of product is 0! No product to be added!");
            } else {
                //Products with 1 page only
                //Add products to cart for the same page
                int hrefCount = page.locator("//a[contains(@href, '/products/')]").count();
                List<String> productLink = new ArrayList<>();
                if (pageSize == 2) {
                    for (int i = 0; i < hrefCount; i++) {
                        productLink.add(page.locator("//a[contains(@href, '/products/')]").nth(i).getAttribute("href"));
                        page.locator("//a[contains(@href, '" + productLink.get(i) + "')]").hover();
                        // Click button:has-text("add to cart")
                        page.locator("//a[contains(@href, '" + productLink.get(i) + "')]/descendant::button").click();
                        // Click button:has-text("Add to cart")
                        page.locator("//button[contains(@class, 'containedPrimary') and @type='button' and @data-test-add-to-cart='true']").click();
                        // Click div:nth-child(3) > div > div > .MuiButtonBase-root
                        page.locator("//button[@data-test-product-detail-modal-close='true']").click();
                        System.out.println("Challenge 6 - Page 1: product " + productLink.get(i) + " has been added to  cart");
                    }
                    productLink.clear();
                    System.out.println("Challenge 6 - All product has been added to cart!");
                }

                //Products with more than 1 page
                //Add products to cart for the last Page
                else {
                    for (int i = 0; i < hrefCount; i++) {
                        productLink.add(page.locator("//a[contains(@href, '/products/')]").nth(i).getAttribute("href"));
                        page.locator("//a[contains(@href, '" + productLink.get(i) + "')]").hover();
                        // Click button:has-text("add to cart")
                        page.locator("//a[contains(@href, '" + productLink.get(i) + "')]/descendant::button").click();
                        // Click button:has-text("Add to cart")
                        page.locator("//button[contains(@class, 'containedPrimary') and @type='button' and @data-test-add-to-cart='true']").click();
                        // Click div:nth-child(3) > div > div > .MuiButtonBase-root
                        page.locator("//button[@data-test-product-detail-modal-close='true']").click();
                        System.out.println("Challenge 6 - Page " + (pageSize-2) + ": product " + productLink.get(i) + " has been added to  cart");
                    }
                    productLink.clear();

                    //Add products to cart for the 2nd to the last page up until Page 1.
                    for (int maxPageSize = pageSize - 2; maxPageSize > 1; maxPageSize--) {
                        page.locator("[aria-label='Go to previous page']").click();
                        int hrefCountPrevPage = page.locator("//a[contains(@href, '/products/')]").count();
                        List<String> productLinkInLoop = new ArrayList<>();
                        for (int i = 0; i < hrefCountPrevPage; i++) {
                            productLinkInLoop.add(page.locator("//a[contains(@href, '/products/')]").nth(i).getAttribute("href"));
                            page.locator("//a[contains(@href, '" + productLinkInLoop.get(i) + "')]").hover();
                            // Click button:has-text("add to cart")
                            page.locator("//a[contains(@href, '" + productLinkInLoop.get(i) + "')]/descendant::button").click();
                            // Click button:has-text("Add to cart")
                            page.locator("//button[contains(@class, 'containedPrimary') and @type='button' and @data-test-add-to-cart='true']").click();
                            // Click div:nth-child(3) > div > div > .MuiButtonBase-root
                            page.locator("//button[@data-test-product-detail-modal-close='true']").click();
                            System.out.println("Challenge 6 - Page " + (maxPageSize - 1) + ": product " + productLinkInLoop.get(i) + " has been added to  cart");
                        }
                        productLinkInLoop.clear();
                    }
                    System.out.println("Challenge 6 - All product has been added to cart!");
                }
            }
            page.locator("button:has-text('CHECK')").click();
            assertThat(page.locator("text=Your answer is correct!")).isVisible();
            page.locator("button:has-text('NEXT')").click();
            System.out.println("-----------CHALLENGE 6 END: ADD ITEMS TO CART-------------");



            //CHALLENGE 7 - FILTER AND FIND PRODUCT THAT MATCHES//
            System.out.println("-----------CHALLENGE 7 START: FIND PRODUCT THAT MATCHES THE FILTER-------------");
            //Tick Category Checkbox
            String Challenge7CategoryToMatch = page.locator("#challenge-7-category").innerText();
            System.out.println("Challenge 7 - Category to match is: " + Challenge7CategoryToMatch);
            //minimize challenge frame
            page.locator("//button[@data-test-minimize= 'true']").click();
            page.locator("//input[@name='" + Challenge7CategoryToMatch + "' and @type='checkbox']").click();
            System.out.println("Challenge 7 - " + Challenge7CategoryToMatch + " checkbox is ticked");
            //maximize challenge frame
            page.locator("//button[@data-test-maximize= 'true']").click();

            //Tick Size Checkbox
            String Challenge7SizeToMatch = page.locator("#challenge-7-size").innerText();
            System.out.println("Challenge 7 - Size to match is: " + Challenge7SizeToMatch);
            //minimize challenge frame
            page.locator("//button[@data-test-minimize= 'true']").click();
            page.locator("//input[@name='" + Challenge7SizeToMatch + "' and @type='checkbox']").click();
            System.out.println("Challenge 7 - " + Challenge7SizeToMatch + " checkbox is ticked");
            //maximize challenge frame
            page.locator("//button[@data-test-maximize= 'true']").click();

            //Adjust Price Range
            int Challenge7minPriceToMatch = Integer.parseInt(page.locator("#challenge-7-min-price").innerText());
            int Challenge7maxPriceToMatch = Integer.parseInt(page.locator("#challenge-7-max-price").innerText());
            System.out.println("Challenge 7 - min price to match is: " + Challenge7minPriceToMatch + "; max price to match is: " + Challenge7maxPriceToMatch);
            String Challenge7priceToggle = page.locator("//p[@data-test-range='true']").innerText();
            System.out.println("Challenge 7 - Toggle price is: " + Challenge7priceToggle);
            int Challenge7minPriceToggle = Integer.parseInt(Challenge7priceToggle.substring(1, Challenge7priceToggle.indexOf("-") - 1));
            System.out.println("Challenge 7 - Toggle min price is: " + Challenge7minPriceToggle);
            int Challenge7maxPriceToggle = Integer.parseInt(Challenge7priceToggle.substring((Challenge7priceToggle.lastIndexOf("$")) + 1));
            System.out.println("Challenge 7 - Toggle max price is: " + Challenge7maxPriceToggle);

            //minimize challenge frame
            page.locator("//button[@data-test-minimize= 'true']").click();

            //reset price range for simplicity
            if (Challenge7minPriceToggle != 50) {
                do {
                    // Press ArrowLeft
                    page.locator("span[role='slider']").first().press("ArrowLeft");
                    Challenge7minPriceToggle--;
                } while (Challenge7minPriceToggle != 50);
            }

            if (Challenge7maxPriceToggle != 2000) {
                do {
                    // Press ArrowRight
                    page.locator("span[role='slider']").nth(1).press("ArrowRight");
                    Challenge7maxPriceToggle++;
                } while (Challenge7maxPriceToggle != 2000);
            }

            System.out.println("Challenge 7 - Done resetting min price and max price");


            //Check and Adjust MIN PRICE and MAX PRICE for Challenge 7
            if (Challenge7minPriceToggle != Challenge7minPriceToMatch) {
                do {
                    page.locator("span[role='slider']").first().press("ArrowRight");
                    Challenge7minPriceToggle++;
                } while (Challenge7minPriceToggle != Challenge7minPriceToMatch);
            }
            System.out.println("Challenge 7 - Toggle min price is now: " + Challenge7minPriceToggle);

            if (Challenge7maxPriceToggle != Challenge7maxPriceToMatch) {
                do {
                    page.locator("span[role='slider']").nth(1).press("ArrowLeft");
                    Challenge7maxPriceToggle--;
                } while (Challenge7maxPriceToggle != Challenge7maxPriceToMatch);
            }
            System.out.println("Challenge 7 - Toggle max price is now: " + Challenge7maxPriceToggle);

            System.out.println("Challenge 7 - All filters have been set!");

            //maximize challenge frame
            page.locator("//button[@data-test-maximize= 'true']").click();
            page.locator("button:has-text('CHECK')").click();
            assertThat(page.locator("text=Your answer is correct!")).isVisible();
            page.locator("button:has-text('NEXT')").click();
            System.out.println("-----------CHALLENGE 7 END: FIND PRODUCT THAT MATCHES THE FILTER-------------");


            //CHALLENGE 8 - Adjust Cart according to price range//
            System.out.println("-----------CHALLENGE 8 START: ADJUST CART ACCORDING TO PRICE RANGE-------------");
            //get min and max price
            int Challenge8minPriceToMatch = Integer.parseInt(page.locator("#challenge-8-min-price").innerText().substring(1));
            int Challenge8maxPriceToMatch = Integer.parseInt(page.locator("#challenge-8-max-price").innerText().substring(1));
            System.out.println("Challenge 8 - min price to match is: " + Challenge8minPriceToMatch + "; max price to match is: " + Challenge8maxPriceToMatch);

            //Go to Cart and check subtotal
            page.locator("//a[@href='/cart']").click();
            int subtotal = Integer.parseInt(page.locator("//h6[@id='subtotal-price']").innerText().substring(1));

            //Get total count of products in the cart
            int grandTotalNumOfProd = page.locator("//div[contains(@class, 'MuiGrid-root MuiGrid-item')]/descendant::img[@data-test-img='true']").count();

            //Check if subtotal is greater than the max price -> then "REMOVE" some products from the cart
            if (subtotal > Challenge8maxPriceToMatch) {
                //remove the product  from the cart 1 by 1
                for (int i=grandTotalNumOfProd; i>1; i--){
                    //remove bottom product
                    page.locator("//div[contains(@class, 'MuiGrid-root MuiGrid-item')]/descendant::img[@data-test-img='true']/ancestor::div[contains(@class, 'MuiGrid-root MuiGrid-item')]/descendant::button[@data-test-remove='true']").nth(0).click();
                    //check if subtotal <= Challenge8maxPriceToMatch
                    if (Integer.parseInt(page.locator("//h6[@id='subtotal-price']").innerText().substring(1)) <=
                            Integer.parseInt(page.locator("#challenge-8-max-price").innerText().substring(1))) {
                        break;
                    }
                }
                System.out.println("Challenge 8 - Subtotal greater than Max Price condition: Cart Adjusted!");
                page.locator("//div[@id='challenges-popup']/descendant::button[@data-test-check='true']").click();
                assertThat(page.locator("text=Your answer is correct!")).isVisible();
                page.locator("button:has-text('NEXT')").click();
                assertThat(page.locator("text=You have done well, my friend.")).isVisible();
            }

            //Check if subtotal is less than the min price -> then "INCREASE" number of items for any product
            if (subtotal < Challenge8minPriceToMatch) {
                do {
                    page.locator("//div[contains(@class, 'MuiGrid-root MuiGrid-item')]/descendant::img[@data-test-img='true']/ancestor::div[contains(@class, 'MuiGrid-root MuiGrid-item')]/descendant::button[@data-test-add='true']").nth(0).click();
                }while (Integer.parseInt(page.locator("//h6[@id='subtotal-price']").innerText().substring(1)) <
                        Integer.parseInt(page.locator("#challenge-8-min-price").innerText().substring(1)));

                System.out.println("Challenge 8 - Subtotal less than Min Price condition: Cart Adjusted!");
                page.locator("//div[@id='challenges-popup']/descendant::button[@data-test-check='true']").click();
                assertThat(page.locator("text=Your answer is correct!")).isVisible();
                page.locator("button:has-text('NEXT')").click();
                assertThat(page.locator("text=You have done well, my friend.")).isVisible();
            }

            //Check if subtotal is in between min price and max price
            if (subtotal >= Challenge8minPriceToMatch && subtotal <= Challenge8maxPriceToMatch) {
                System.out.println("Challenge 8 - Condition is perfect! No need to adjust anything from the cart!");
                page.locator("//div[@id='challenges-popup']/descendant::button[@data-test-check='true']").click();
                assertThat(page.locator("text=Your answer is correct!")).isVisible();
                page.locator("button:has-text('NEXT')").click();
                assertThat(page.locator("text=You have done well, my friend.")).isVisible();
            }
            System.out.println("-----------CHALLENGE 8 END: ADJUST CART ACCORDING TO PRICE RANGE-------------");

        }
    }
}
