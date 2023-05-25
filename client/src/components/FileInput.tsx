import { FormEvent, useState } from "react";
import LoadingBar from "./LoadingBar";

export default function FileInput() {
  const [loading, setLoading] = useState<Boolean>(false);
  const handleUpload = (e: FormEvent) => {
    e.preventDefault();
    fetch("/api/test").then((res) =>
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
      <form
        onSubmit={(e) => {
          setLoading(true);
          handleUpload(e);
        }}
      >
        <input
          type="file"
          name="file"
          id="file"
          className="file-input file-input-bordered file-input-secondary"
        />
        <button type="submit" className="btn btn-outline btn-info">
          Compress
        </button>
      </form>
    </div>
  );
}
