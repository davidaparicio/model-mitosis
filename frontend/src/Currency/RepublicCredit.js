import React from "react";
import {withRouter} from "react-router-dom";


import SvgIcon from "@material-ui/core/SvgIcon";
import {makeStyles} from "@material-ui/core/styles";

function RepublicCredit() {
    const useStyles = makeStyles(theme => ({
        republicCredit: {
            marginLeft: "2px",
            fontSize: "1.3em"
        }
    }));
    const classes = useStyles();
    return (
        <SvgIcon className={classes.republicCredit}>
            <path id="r1"
                  d="M1.24,0V4.35H0v5H1.26v4.33h.92V9.33H4.06v4.36H5V9.33H7.16L1.63,22.39H5.06l7.1-18H5V0H4.06V4.35H2.2V0Z"/>
        </SvgIcon>
    );
}

export default withRouter(RepublicCredit);
