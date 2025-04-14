document.addEventListener('DOMContentLoaded', () => {
  const collectionContainer = document.querySelectorAll('.collectionContainer');
  const body = document.body;
  const html = document.documentElement;

  // Iterate over each collectionContainer
  collectionContainer.forEach((collectionContainer) => {
    const collectionContainerNavbar = collectionContainer.querySelector('.collectionContainerNavbar');
    const collectionImage = collectionContainer.querySelector('.collectionImage');
    const collectionCaption = collectionContainer.querySelector('.collectionCaption');
    const collectionCommentButton = collectionContainer.querySelector('.collectionCommentButton');
    const collectionCommentBoxExit = collectionContainer.querySelector('.collectionCommentBoxExit');
    const collectionCommentBox = collectionContainer.querySelector('.collectionCommentBox');
    const footerNavContent = document.getElementById('footerNavContent');
    const footerComment = document.getElementById('commentActivity');


    collectionCommentButton.addEventListener('click', () => {


      if (collectionCommentBox.classList.contains('hidden')) {

        // Collection Full Screen
        collectionContainer.classList.add('fullscreen');

        //Disable Page Scrolling
        body.classList.add('overflow-hidden');
        html.classList.add('overflow-hidden');

        // Show Comment Box
        collectionCommentBox.classList.remove('hidden');

        //Show Comment Box Exit
        collectionCommentBoxExit.classList.remove('hidden');

        // Exit Comment Box
        collectionCommentBoxExit.addEventListener('click', () => {
          // Hide Comment Box
          collectionCommentBox.classList.add('hidden');

          // Show Comment Box Exit
          collectionCommentBoxExit.classList.add('hidden');

          // Enable Page Scrolling
          body.classList.remove('overflow-hidden');
          html.classList.remove('overflow-hidden');

          // Collection Full Screen
          collectionContainer.classList.remove('fullscreen');
          footerNavContent.classList.remove('hidden');
          footerComment.classList.add('hidden');

        });
        
        // Calculate Comment Box Height
        const footer = document.querySelector('footer');
        const footerTop = footer.getBoundingClientRect().top;
        const captionBottom = collectionCaption.getBoundingClientRect().bottom;
        const availableHeight = Math.max(0, footerTop - captionBottom);

        // Set the height of the comment box
        collectionCommentBox.style.maxHeight = `${availableHeight}px`;

        // Add Scrolling to Comment Box
        collectionCommentBox.style.overflowY = 'auto';

        // Add padding to the bottom of the comment box
        collectionCommentBox.style.paddingBottom = '20px';

        //Footer Content
        footerNavContent.classList.add('hidden');
        footerComment.classList.remove('hidden');


      } else {

        // Reset Index Page
        collectionCommentBox.classList.add('hidden');
        collectionCommentBoxExit.classList.add('hidden');
        body.classList.remove('overflow-hidden');
        html.classList.remove('overflow-hidden');
        collectionContainer.classList.remove('fullscreen');
        footerNavContent.classList.remove('hidden');
        footerComment.classList.add('hidden');
      }
    });


  })
});