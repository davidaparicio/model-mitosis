import React from "react";
import Typography from "@material-ui/core/Typography";

export default function Class({ comfortClass, className }) {
  const formattedClass = comfortClass === "FIRST" ? "1st Class" : "2nd Class";
  return <Typography color="secondary" variant="body2" className={className}>{formattedClass}</Typography>;
}
