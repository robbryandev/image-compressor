import { FormEvent, useState } from "react";
import LoadingBar from "./LoadingBar";

export default function FileInput() {
  const [loading, setLoading] = useState<Boolean>(false);
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
    fetch("/api/upload", {
      method: "POST",
      body: formData,
    }).then((res) =>
      res.json().then((jres) => {
        console.log(jres);
        setTimeout(() => {
          setLoading(false);
        }, 1500);
      })
    );
  };

  return (
    <div className="space-y-4">
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
