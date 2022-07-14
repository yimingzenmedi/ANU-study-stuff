import React from "react";
import styles from "./index.module.less";
import {withRouter, Link} from "react-router-dom";


class Start extends React.Component {

  render() {
    return (
      <div className={styles.wrap}>
        <header className="App-header">
          {/*<img src={logo} className="App-logo" alt="logo" />*/}
          <div className={styles.bgPic}/>
          <div className={styles.cover1} />
          <div className={styles.cover2} />
        </header>

        <div className={styles.main}>
          <p className={styles.group}>COMP6250 Group 3</p>
          <h1>Professional Practice</h1>
          <p className={styles.description}>
            This website demonstrates how to think and practice in work through
            interaction in situations, and give ideas about how to become a
            responsible professional.
          </p>

          <Link to={"/quiz"} className={styles.btn}>
            <div>
              Let's start!
            </div>
          </Link>

        </div>
      </div>
    )
  }
}

export default withRouter(Start);
