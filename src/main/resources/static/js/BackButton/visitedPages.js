document.addEventListener('DOMContentLoaded', function () {
  // CONSTRUCTOR FOR PAGE ITEM
  class PageItem {
    constructor(url, scrollY, isBack) {
      this.url = url;
      this.scrollY = scrollY;
      this.isBack = isBack;
    }
  }

  // GETTING VISITED PAGES FROM SESSION STORAGE
  let visitedPages = JSON.parse(sessionStorage.getItem('visitedUrls')) || [];

  // ADDING CURRENT PAGE TO VISITED PAGES
  const currentUrl = window.location.href;

  // Check if the navigation was triggered by the back button
  const isNavigatingBack = sessionStorage.getItem('isNavigatingBack') === 'true';

  if (isNavigatingBack) {
    // Clear the isNavigatingBack flag immediately after processing
    sessionStorage.setItem('isNavigatingBack', 'false');
    console.log('isNavigatingBack flag cleared.');
  }

  // Add the current page to visitedPages if it's not already in the list
  if (!isNavigatingBack && !visitedPages.some((page) => page.url === currentUrl && page.isBack)) {
    const pageItem = new PageItem(currentUrl, window.scrollY, false); // isBack = false for normal navigation
    visitedPages.push(pageItem);
    sessionStorage.setItem('visitedUrls', JSON.stringify(visitedPages));
    console.log('Added new page to visited pages:', pageItem);
  } else {
    console.log('Page is marked as isBack or navigation was triggered by back button and will not be added:', currentUrl);
  }

  // BACK BUTTON FUNCTIONALITY
  const backButton = document.getElementById('backButton');
  if (backButton) {
    backButton.addEventListener('click', function () {
      if (visitedPages.length > 1) {
        // Remove the current page
        visitedPages.pop();

        // Get the last visited page
        const lastVisitedPage = visitedPages[visitedPages.length - 1];
        sessionStorage.setItem('visitedUrls', JSON.stringify(visitedPages));

        // Mark the last visited page as isBack = true
        lastVisitedPage.isBack = true;
        console.log('Navigating back to:', lastVisitedPage);

        // Set the isNavigatingBack flag
        sessionStorage.setItem('isNavigatingBack', 'true');

        // Navigate to the last visited page
        window.location.href = lastVisitedPage.url;
      } else {
        console.log('No previous pages to go back to.');
      }
    });
  }

  // HOME BUTTON FUNCTIONALITY
  const homeButton = document.getElementById('homeButton');
  const logoHomeButton = document.getElementById('logoHomeButton');

  if (homeButton) {
    homeButton.addEventListener('click', function (event) {
      // Prevent the default navigation behavior
      event.preventDefault();

      // Set a flag to ignore scrollY restoration
      sessionStorage.setItem('ignoreScrollY', 'true');

      // Clear the visited pages array
      visitedPages = [];
      sessionStorage.setItem('visitedUrls', JSON.stringify(visitedPages));
      console.log('Visited pages cleared on home button click.');

      // Reset the scroll position to 0
      window.scrollTo(0, 0);

      // Add the index page to the visitedUrls array with scrollY = 0
      const indexPage = {
        url: '/index',
        scrollY: 0,
        isBack: false,
      };
      visitedPages.push(indexPage);
      sessionStorage.setItem('visitedUrls', JSON.stringify(visitedPages));
      console.log('Index page added to visitedUrls with scrollY = 0:', indexPage);

      // Navigate to the index page
      window.location.href = homeButton.href;
    });
  }

  if (logoHomeButton) {
    logoHomeButton.addEventListener('click', function (event) {
      // Prevent the default navigation behavior
      event.preventDefault();

      // Set a flag to ignore scrollY restoration
      sessionStorage.setItem('ignoreScrollY', 'true');

      // Clear the visited pages array
      visitedPages = [];
      sessionStorage.setItem('visitedUrls', JSON.stringify(visitedPages));
      console.log('Visited pages cleared on home button click.');

      // Reset the scroll position to 0
      window.scrollTo(0, 0);

      // Add the index page to the visitedUrls array with scrollY = 0
      const indexPage = {
        url: '/index',
        scrollY: 0,
        isBack: false,
      };
      visitedPages.push(indexPage);
      sessionStorage.setItem('visitedUrls', JSON.stringify(visitedPages));
      console.log('Index page added to visitedUrls with scrollY = 0:', indexPage);

      // Navigate to the index page
      window.location.href = homeButton.href;
    });
  }

  console.log('Visited Pages:', visitedPages);

  // SAVE SCROLL POSITION ON BEFOREUNLOAD
  window.addEventListener('beforeunload', function () {
    const currentScrollY = window.scrollY;

    // Update the scrollY value for the current page in visitedPages
    const currentPage = visitedPages.find((page) => page.url === currentUrl);
    if (currentPage) {
      currentPage.scrollY = currentScrollY;
      sessionStorage.setItem('visitedUrls', JSON.stringify(visitedPages));
      console.log('Updated scroll position for current page:', currentPage);
    }
  });

  // RESTORE SCROLL POSITION ON PAGE LOAD
  window.addEventListener('load', function () {
    // Check if the ignoreScrollY flag is set
    const ignoreScrollY = sessionStorage.getItem('ignoreScrollY') === 'true';

    if (ignoreScrollY) {
      // Clear the flag and skip restoring scrollY
      sessionStorage.setItem('ignoreScrollY', 'false');
      console.log('Ignoring scrollY restoration for this navigation.');
      return;
    }

    const currentPage = visitedPages.find((page) => page.url === currentUrl);
    if (currentPage && currentPage.scrollY !== undefined) {
      // Restore the scroll position
      window.scrollTo(0, currentPage.scrollY);
      console.log('Restored scroll position to:', currentPage.scrollY);
    }
  });
});