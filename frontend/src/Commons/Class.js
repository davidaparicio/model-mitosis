import React from "react";

export default function Class({ comfortClass, className }) {
  const formattedClass = comfortClass === "FIRST" ? "1st Class" : "2nd Class";
  return <div className={className}>{formattedClass}</div>;
}
