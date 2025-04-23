
function timeSince(Timestamp) {
    // Convert the timestamp to a Date object
    const date = new Date(Timestamp);

    // Calculate the difference in seconds between the current time and the given date
    // const seconds = Math.floor((new Date() - date) / 1000);
    // Calculate the difference in seconds between the current time and the given date
    // const seconds = Math.floor((new Date() - date) / 1000);

    const seconds = Math.floor((new Date() - date) / 1000);
    
    let interval = Math.floor(seconds / 31536000);

    if (interval > 1) {
        return interval + " years";
    }
    interval = Math.floor(seconds / 2592000);
    if (interval > 1) {
        return interval + " months";
    }
    interval = Math.floor(seconds / 86400);
    if (interval > 1) {
        return interval + " days";
    }
    interval = Math.floor(seconds / 3600);
    if (interval > 1) {
        return interval + " hours";
    }
    interval = Math.floor(seconds / 60);
    if (interval > 1) {
        return interval + " minutes";
    }
    return seconds + " seconds";
}