import React from 'react'
import { makeStyles } from '@material-ui/core/styles'
import AppBar from '@material-ui/core/AppBar'
import Toolbar from '@material-ui/core/Toolbar'
import Typography from '@material-ui/core/Typography'
import logo from './mandalore-express.png'
import SearchForm from './SearchForm/SearchForm'
import Search from './Search/Search'
import { createTheme } from '@material-ui/core/styles'
import { ThemeProvider } from '@material-ui/styles'
import Link from '@material-ui/core/Link'

import { BrowserRouter as Router, Switch, Route } from 'react-router-dom'
const useStyles = makeStyles(theme => ({
  root: {
    flexGrow: 1,
    'overflow': 'hidden',
    minHeight: '100vh',
    backgroundColor: '#696969'
  },
  menuButton: {
    marginRight: theme.spacing(2)
  },
  title: {
    flexGrow: 1,
  },
  logo: {
    width: '32px',
    marginRight: theme.spacing(1)
  }
}))

const theme = createTheme({
  palette: {
    primary: {
      main: '#414141'
    },
    secondary: {
      main: '#B98740'
    }
  }
})

function App() {
  const classes = useStyles()
  return (
    <ThemeProvider theme={theme}>
      <div className={classes.root}>
        <AppBar position='static'>
          <Toolbar>
            <img src={logo} className={classes.logo} alt='logo' />
            <Typography variant='h6' className={classes.title}>
              <Link href='/' color='inherit' underline='none'>
                Mandalore Express
              </Link>
            </Typography>
          </Toolbar>
        </AppBar>
        <Router>
          <Switch>
            <Route path='/searches/:searchId'>
              <Search />
            </Route>
            <Route path='/'>
              <SearchForm />
            </Route>
          </Switch>
        </Router>
      </div>
    </ThemeProvider>
  )
}

export default App
