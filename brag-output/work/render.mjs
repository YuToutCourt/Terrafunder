import { chromium } from '/opt/node22/lib/node_modules/playwright/index.mjs';
import fs from 'fs';
const [mode, ...rest] = process.argv.slice(2);
const FPS = 30, DUR = 23.5;
const browser = await chromium.launch({ args: ['--allow-file-access-from-files'] });
const page = await browser.newPage({ viewport: { width: 1920, height: 1080 } });
await page.goto('file://' + process.cwd() + '/index.html');
await page.evaluate(() => window.READY);
if (mode === 'stills') {
  fs.mkdirSync('stills', { recursive: true });
  for (const t of rest.map(Number)) { await page.evaluate(t => render(t), t); await page.screenshot({ path: `stills/t${t.toFixed(2)}.png` }); }
} else {
  fs.mkdirSync('frames', { recursive: true });
  const N = Math.round(FPS * DUR);
  for (let f = 0; f < N; f++) {
    await page.evaluate(t => render(t), f / FPS);
    await page.screenshot({ path: `frames/f${String(f).padStart(4, '0')}.jpg`, type: 'jpeg', quality: 95 });
  }
  console.log(JSON.stringify(await page.evaluate(() => window.CHAT_STARTS)));
}
await browser.close();
