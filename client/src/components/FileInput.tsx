import { FormEvent, useRef, useState } from "react";
import LoadingBar from "./LoadingBar";
import download from "../utils/download";

export default function FileInput() {
  const [loading, setLoading] = useState<Boolean>(false);
  const dataRef = useRef<HTMLDivElement>(null);
  const handleUpload = (e: FormEvent) => {
    e.preventDefault();
    setLoading(true);
    const formData = new FormData();
    const target: any = e.currentTarget;
    let files: File[] = target.file.files;
    if (files) {
      for (let i = 0; i < files.length; i++) {
        formData.append("file", files[i]);
      }
    }
    fetch("http://localhost:8080/api/upload", {
      method: "POST",
      body: formData,
    }).then((res) => {
      res.blob().then((data) => {
        download(dataRef, `min_${files[0].name}`, data);
        setLoading(false);
      });
    }).catch((err) => {
      console.log(`post error: ${err}`)
    });
  };

  return (
    <div className="space-y-4">
      <div id="download-spot" ref={dataRef}></div>
      {loading && <LoadingBar />}
      <form onSubmit={handleUpload}>
        <input
          type="file"
          name="file"
          id="file"
          accept=".jpg, .jpeg, .png"
          className="file-input file-input-bordered file-input-secondary"
          required
        />
        <button type="submit" className="btn btn-outline btn-info">
          Compress
        </button>
      </form>
    </div>
  );
}
