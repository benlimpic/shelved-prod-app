window.passCollectionId = function(collectionId) {
  console.log("passCollectionId function called with collectionId: " + collectionId);

  // Store the collection ID in a hidden input field
  const commentFormInput = document.querySelector('.commentFormCollectionIdInput');
  if (commentFormInput) {
      commentFormInput.setAttribute('value', collectionId);
  }

  // Set the action attribute of the form to include the collection ID
  const commentForm = document.querySelector('.commentForm');
  if (commentForm) {
      commentForm.setAttribute("action", `/collections/${collectionId}/comments-from-index`);
  }
};