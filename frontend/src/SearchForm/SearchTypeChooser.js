import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import Radio from "@material-ui/core/Radio";
import RadioGroup from "@material-ui/core/RadioGroup";
import FormControlLabel from "@material-ui/core/FormControlLabel";
import FormControl from "@material-ui/core/FormControl";
import TrendingFlatIcon from "@material-ui/icons/TrendingFlat";
import SyncAltIcon from "@material-ui/icons/SyncAlt";
import { Box } from "@material-ui/core";

const useStyles = makeStyles(theme => ({
  option: {
    marginLeft: theme.spacing(1)
  },
  radioGroup: {
    flexDirection: "row",
    justifyContent: "space-between"
  },
  formControl: {
    width: "100%",
    marginBottom: theme.spacing(1)
  }
}));

export default function SearchTypeChooser({ searchType, setSearchType }) {
  const classes = useStyles();
  const handleChange = event => {
    setSearchType(event.target.value);
  };

  return (
    <div>
      <FormControl component="fieldset" className={classes.formControl}>
        <RadioGroup
          className={classes.radioGroup}
          aria-label="search type"
          name="searchTypeChooser"
          value={searchType}
          onChange={handleChange}
        >
          <FormControlLabel
            value="oneway"
            control={<Radio color="primary" />}
            label={
              <Box display="flex" alignContent="center">
                <TrendingFlatIcon />
                <div className={classes.option}>One Way</div>
              </Box>
            }
          />
          <FormControlLabel
            value="roundtrip"
            control={<Radio color="primary" />}
            label={
                <Box display="flex" alignContent="center">
                <SyncAltIcon />
                <div className={classes.option}>Round Trip</div>
              </Box>
            }
          />
        </RadioGroup>
      </FormControl>
    </div>
  );
}
