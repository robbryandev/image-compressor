import { RefObject } from "react";

export default function download(
  ref: RefObject<HTMLDivElement>,
  fileName: string,
  blob: Blob
) {
  if (fileName) {
    let a = document.createElement("a");
    ref.current?.appendChild(a);
    a.download = fileName;
    a.href = window.URL.createObjectURL(blob);
    a.click();
    setTimeout(() => {
      ref.current?.removeChild(a);
    }, 100);
  }
}
