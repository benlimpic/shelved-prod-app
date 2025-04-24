document.addEventListener('DOMContentLoaded', function () {
  // Check if the URL contains a hash with the format #comments-{collectionId}
  if (window.location.hash && window.location.hash.startsWith('#comments-')) {
    const hash = window.location.hash;
    const collectionId = hash.split('-')[1]; // Extract the collection ID from the hash

    console.log('Detected collection ID from hash:', collectionId);

    const commentBox = document.querySelector('.collectionCommentBox');
    const footerComment = document.getElementById('commentActivity');
    const footerNavContent = document.getElementById('footerNavContent');

    commentBox.classList.remove('hidden');
    footerComment.classList.remove('hidden');
    footerNavContent.classList.add('hidden');

    // Pass the collection ID to the footer
    const commentFormInput = document.querySelector(
      '.commentFormCollectionIdInput'
    );
    if (commentFormInput) {
      commentFormInput.setAttribute('value', collectionId);
    }

    // Set the action attribute of the form to include the collection ID
    const commentForm = document.querySelector('.commentForm');
    if (commentForm) {
      commentForm.setAttribute(
        'action',
        `/collections/${collectionId}/comments-from-index`
      );
    }
  }
});
