import { Metadata } from "next";

export const metadata: Metadata = {
  title: "게시판",
};

export default function HomePage() {
  return (
    <div className="flex">
      <h1 className="text-3xl underline">Simple Board</h1>
    </div>
  );
}
