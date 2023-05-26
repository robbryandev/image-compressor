import { RefObject } from "react";

export default function download(
  ref: RefObject<HTMLDivElement>,
  fileName: string,
  content: string
) {
  if (fileName) {
    let bb = new Blob([content], { type: "text/plain" });
    let a = document.createElement("a");
    ref.current?.appendChild(a);
    a.download = fileName;
    a.href = window.URL.createObjectURL(bb);
    a.click();
    setTimeout(() => {
      ref.current?.removeChild(a);
    }, 100);
  }
}
