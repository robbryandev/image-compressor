function progressToggle() {
  const bar = document.getElementById("progress-bar");
  let hasBar = bar.classList.contains("hidden");
  if (hasBar) {
    bar.classList.remove("hidden");
  } else {
    bar.classList.add("hidden");
  }
}
