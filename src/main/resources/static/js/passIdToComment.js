

const triggerCommentId = (button) => {
  if (!button) {
    console.error("Button element is null or undefined.");
    return;
  }

  const commentId = button.getAttribute('data-comment-id');
  if (!commentId) {
    console.error("Button does not have a 'data-comment-id' attribute.");
    return;
  }

  const commentReplyActivity = document.getElementById('commentReplyActivity');
  const commentActivity = document.getElementById('commentActivity');
  const commentIdInput = document.getElementById('replyCommentIdInput');

  if (commentIdInput) {
    commentIdInput.setAttribute('value', commentId);
    console.log(`Hidden input value set to: ${commentId}`);
  } else {
    console.error("Hidden input with ID 'replyCommentIdInput' not found.");
  }

  if (commentActivity && commentReplyActivity) {
      commentActivity.classList.add('hidden');
      commentReplyActivity.classList.remove('hidden');
  }
  else {
    console.error("Comment activity or reply activity element not found.");
  }
};



const triggerReplyId = (button) => {

if (!button) {
    console.error("Button element is null or undefined.");
    return;
  }

  const commentId = button.getAttribute('data-comment-id');
  if (!commentId) {
    console.error("Button does not have a 'data-comment-id' attribute.");
    return;
  }

  let usernameSlug = "";

  const username = button.getAttribute('data-reply-username');
  if (!username || username.trim() === "") {
    usernameSlug = "";
    console.error("Button does not have a 'data-reply-username' attribute or it is empty.");
    return;
  }
  usernameSlug = "@" + username + " ";
  

  const commentReplyActivity = document.getElementById('commentReplyActivity');
  const commentActivity = document.getElementById('commentActivity');
  const commentIdInput = document.getElementById('replyCommentIdInput');
  const replyContentInput = document.getElementById('replyContentInput');




  if (commentIdInput) {
    commentIdInput.setAttribute('value', commentId);
    console.log(`Hidden input value set to: ${commentId}`);
  } else {
    console.error("Hidden input with ID 'replyCommentIdInput' not found.");
  }


  if (!replyContentInput) {
    console.error("Hidden input with ID 'replyContentInput' not found.");
    return;
  }

  if (replyContentInput) {
    const currentValue = replyContentInput.value;
    const newValue = usernameSlug;
    replyContentInput.setAttribute('value', newValue);
    console.log(`Hidden input value set to: ${newValue}`);
  }

  if (commentActivity && commentReplyActivity) {
    commentActivity.classList.add('hidden');
    commentReplyActivity.classList.remove('hidden');
  }
  else {
    console.error("Comment activity or reply activity element not found.");
  }

};

