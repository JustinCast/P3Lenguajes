const express = require("express");
const bodyParser = require("body-parser");
const app = express();

/**
 * Server config
 */

function config() {
  app.use(bodyParser.urlencoded({ extended: true }));
  app.use(bodyParser.json());
  app.use((req, res, next) => {
    res.header("Access-Control-Allow-Origin", "http://localhost:4200");
    res.header(
      "Access-Control-Allow-Methods",
      "GET, POST, PUT, DELETE, OPTIONS"
    );
    res.header(
      "Access-Control-Allow-Headers",
      "Origin, X-Requested-With, Content-Type, Accept, Authorization, Access-Control-Allow-Credentials"
    );
    res.header("Access-Control-Allow-Credentials", "true");
    next();
  });
}

function router() {
    app.post("/resolve", (req, res) => {
		getPath(req.body.from, req.body.to);
	});
}

function getPath(from, to) {
  const Request = require("request");
  Request.post(
    {
      headers: { "content-type": "application/json" },
      url: "http://localhost:2000/resolve",
      body: JSON.stringify({
        from: from,
        to: to
      })
    },
    (error, response, body) => {
      if (error) {
        return console.dir(error);
      }
      console.dir(JSON.parse(body));
    }
  );
}
config();
router();
//getPath(1, 5);
app.listen(process.env.PORT || 3000);
