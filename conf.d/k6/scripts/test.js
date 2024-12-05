import http from 'k6/http';
import { check, sleep } from 'k6';
import { Counter } from 'k6/metrics';

let logCounter = new Counter('generated_logs');

export let options = {
  stages: [
    { duration: '1m', target: 2 },    // Ramp up to 2 users over 1 minute
    { duration: '3m', target: 5 },    // Increase to 5 users over 3 minutes
    { duration: '1m', target: 0 },    // Ramp down to 0 users
  ],
  thresholds: {
    http_req_failed: ['rate<0.01'],   // Http errors should be less than 1%
  },
};

export default function () {
  try {
    // Use the correct service name from docker-compose
    let loginRes = http.post('http://isabe:8080/api/authenticate', {
      username: 'admin',
      password: 'admin',
      rememberMe: false
    });

    if (!check(loginRes, {
      'login status was 200': (r) => r.status == 200,
    })) {
      console.log(`Login failed with status ${loginRes.status}`);
      sleep(5);
      return;
    }

    let idToken = loginRes.json('id_token');
    console.log(`Generated log entry for login at ${new Date().toISOString()}`);
    logCounter.add(1);

    sleep(2); // Wait 2 seconds between requests

    let headers = {
      headers: {
        Authorization: `Bearer ${idToken}`,
      },
    };

    let getContadorRes = http.get('http://isabe:8080/api/getContador', headers);
    check(getContadorRes, {
      'getContador status was 200': (r) => r.status == 200,
    });
    console.log(`Generated log entry for getContador at ${new Date().toISOString()}`);
    logCounter.add(1);

    sleep(2); // Wait 2 seconds between requests

    let contadorPostRes = http.post('http://isabe:8080/api/contadorPost', { value: 1 }, headers);
    check(contadorPostRes, {
      'contadorPost status was 200': (r) => r.status == 200,
    });
    console.log(`Generated log entry for contadorPost at ${new Date().toISOString()}`);
    logCounter.add(1);

    sleep(3); // Wait 3 seconds before next iteration
  } catch (error) {
    console.error(`Error during test execution: ${error.message}`);
    sleep(5);
  }
}