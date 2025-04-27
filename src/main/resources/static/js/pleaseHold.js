

<!-- Reply Box -->
<div id="replyDiv" class="w-full">
  <!-- Reply Template -->
  <div id="replyTemplate" class="py-1 w-full">
    <div
      class="w-full h-auto flex flex-row justify-start items-start text-sm py-2"
    >
      <!-- Profile Picture -->
      <div id="imageBoxComment" class="flex-shrink-0 w-10">
        <a href="/profile">
          <img
            class="h-7 w-7 rounded-full bg-white"
            th:src="${user?.profilePictureUrl ?: '/images/default-profile-photo.png'}"
            alt="Profile Picture"
          />
        </a>
      </div>

      <!-- Content Box -->
      <div
        id="contentBoxComment"
        class="flex flex-col items-start w-full"
      >
        <div
          class="flex flex-row justify-start items-start w-full"
        >
          <!-- Profile Link -->
          <div class="flex items-start">
            <a href="/profile" class="flex items-start">
              <span
                style="font-size: 10px; line-height: 1"
                class="font-semibold leading-[1]"
                th:if="${user?.username != null}"
                th:text="${user?.username}"
              ></span>
            </a>
          </div>

          <!-- Time Container -->
          <div class="pl-2 flex items-end">
            <span
              style="font-size: 8px; line-height: 1"
              class="font-thin leading-[1]"
            >
              6h
            </span>
          </div>
        </div>

        <div class="flex flex-row justify-start items-start">
          <span
            class="font-light truncate-multiline"
            onclick="toggleTruncate(this)"
            th:text="'This is a placeholder text for the comment content. We are unsure exactly how long it needs to be to invoke the truncate feature.'"
          ></span>
        </div>

        <div
          style="font-size: 8px; line-height: 1"
          class="w-full flex flex-col justify-center items-start pt-1 pr-2 text-slate-500 font-semibold"
        >
          <span class="leading-[1.2]">Reply</span>
        </div>
      </div>

      <!-- Like Box -->
      <div
        id="likeBox"
        class="flex flex-col justify-start items-center pr-2"
      >
        <div class="flex flex-row justify-center items-center">
          <a>
            <span
              style="font-size: 10px; line-height: 1"
              class="font-medium text-xs"
            >
              &#9825;
            </span>
          </a>
        </div>
        <div class="flex flex-row justify-center items-center">
          <p
            style="font-size: 10px; line-height: 1"
            class="font-thin text-xs"
          >
            0
          </p>
        </div>
      </div>
    </div>
  </div>
</div>