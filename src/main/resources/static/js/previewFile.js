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

    // Client-side check for HEIC
  if (file && file.type === "image/heic") {
    alert("HEIC images are not supported. Please upload a JPEG or PNG image.");
    fileInput.value = ""; // Reset the input
    return;
  }

  if (file) {
    const reader = new FileReader();

    reader.onload = function (e) {
      const img = new Image();
      img.onload = function () {

        EXIF.getData(img, function () {
          const orientation = EXIF.getTag(this, 'Orientation');
          console.log('%cEXIF orientation: ' + orientation, 'color: red; font-weight: bold; font-size: 16px;');
          // ...rest of your code...
        });

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

        canvas.toBlob(
          function (blob) {
            const processedFile = new File([blob], 'collection.jpg', {
              type: 'image/jpeg',
            });
            const dataTransfer = new DataTransfer();
            dataTransfer.items.add(processedFile);
            fileInput.files = dataTransfer.files;
            previewImage.src = canvas.toDataURL('image/jpeg');

            // Reference the correct form
            let activeForm = fileInput.closest('form');
          },
          'image/jpeg',
          0.95
        );
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
