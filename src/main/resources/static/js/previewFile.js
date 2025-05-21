const fileInput = document.getElementById('hiddenFileInput');
const previewImage = document.getElementById('previewImage');
const triggerUpload = document.getElementById('triggerUpload');

const createCollectionForm = document.getElementById('createCollectionForm');
const createItemForm = document.getElementById('createItemForm');
const updateProfileForm = document.getElementById('updateProfileForm');
const updateCollectionForm = document.getElementById('updateCollectionForm');
const updateItemForm = document.getElementById('updateItemForm');
let processedBlob = null;

fileInput.addEventListener('change', function () {
  const file = this.files[0];

  if (file) {
    const reader = new FileReader();

    reader.onload = function (e) {
      const img = new Image();
      img.onload = function () {
        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d');
        const size = Math.min(img.width, img.height);
        canvas.width = 500;
        canvas.height = 500;

        ctx.drawImage(
          img,
          (img.width - size) / 2,
          (img.height - size) / 2,
          size,
          size,
          0,
          0,
          canvas.width,
          canvas.height
        );

        canvas.toBlob(function (blob) {
            if (blob) {
                const processedFile = new File([blob], 'collection.jpg', { type: 'image/jpeg' });
                const dataTransfer = new DataTransfer();
                dataTransfer.items.add(processedFile);

                // Find the closest form to the file input
                const closestForm = fileInput.closest('form');
                if (closestForm) {
                    // Find the file input within the closest form and set its files
                    const formFileInput = closestForm.querySelector('input[type="file"]');
                    if (formFileInput) {
                        formFileInput.files = dataTransfer.files;
                    }
                }

                previewImage.src = canvas.toDataURL('image/jpeg');
            } else {
                console.error('Failed to create blob from canvas.');
            }
        }, 'image/jpeg', 0.95);
      };
      img.src = e.target.result;
    };
    reader.readAsDataURL(file);
  }
});


document
  .getElementById('triggerUpload')
  .addEventListener('click', function (event) {
    event.preventDefault(); // Prevent the anchor from navigating
    document.getElementById('hiddenFileInput').click();
  });
