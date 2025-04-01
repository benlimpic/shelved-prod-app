document.addEventListener('DOMContentLoaded', () => {
  const saveChangesButton = document.getElementById('saveChangesButton'); // Button in the nav
  const hiddenSubmitButton = document.getElementById('hiddenSubmitButton'); // Hidden submit button in the form

  // Add click event listener to the nav button
  saveChangesButton.addEventListener('click', () => {
    hiddenSubmitButton.click(); // Programmatically click the hidden submit button
  });
});

{/* <form
action="/create_collection"
method="POST"
enctype="multipart/form-data"
>
<!-- CSRF Token (if applicable, for Spring Security) -->
<input type="hidden" name="_csrf" value="${_csrf.token}" />

<!-- File Upload Field -->
<div class="labelInputBox">
  <img
    class="h-32 w-32 rounded-full bg-white"
    id="previewImage"
    th:src="${'/images/collection_image.png'}"
    alt="Default Collection Image"
  />
  <a href="#" id="triggerUpload" class="mt-2">
    <span class="text-sm font-semibold text-blue-500"
      >Update Collection Cover</span
    >
  </a>
  <input
    type="file"
    id="hiddenFileInput"
    name="collectionImage"
    style="display: none"
    accept="image/*"
  />
</div>

<!-- Caption Field -->

<div class="labelInputBox">
  <label class="label" for="caption">Caption:</label>
  <input
    class="input"
    type="text"
    id="caption"
    name="caption"
    placeholder="Caption"
  />
</div>

<!-- Description Field -->

<div class="labelInputBox">
  <label class="label" for="description">Description:</label>
  <textarea
    class="input"
    type="text"
    id="description"
    name="description"
    placeholder="Description"
  ></textarea>
</div>

<!-- Submit Button -->
<div>
  <button class="button" type="submit">Create Collection</button>
</div>
</form> */}