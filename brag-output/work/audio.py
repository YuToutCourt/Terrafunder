# Original chiptune score + SFX for the Terrafunder brag video. A minor, 120 BPM, 23.5 s.
import numpy as np, wave
SR = 44100; DUR = 23.5; N = int(SR * DUR)
mixL = np.zeros(N); mixR = np.zeros(N)
BEAT = 0.5
rng = np.random.default_rng(4)
def hz(m): return 440.0 * 2 ** ((m - 69) / 12)
def env(n, a=0.005, d=0.1, s=0.6, r=0.08, hold=None):
    t = np.arange(n) / SR; hold = hold if hold is not None else n / SR - r
    e = np.where(t < a, t / a, np.where(t < a + d, 1 - (1 - s) * (t - a) / d, s))
    rel = np.clip((t - hold) / r, 0, 1); return e * (1 - rel)
def lp(x, fc):
    a = np.exp(-2 * np.pi * fc / SR); y = np.zeros_like(x); acc = 0.0
    for i in range(len(x)): acc = (1 - a) * x[i] + a * acc; y[i] = acc
    return y
def put(sig, t, gain=1.0, pan=0.0):
    i = int(t * SR); j = min(N, i + len(sig))
    if i >= N: return
    s = sig[:j - i] * gain
    mixL[i:j] += s * (1 - max(0, pan)); mixR[i:j] += s * (1 + min(0, pan))
def square(f, dur, duty=0.25):
    t = np.arange(int(dur * SR)) / SR; ph = (t * f) % 1; return np.where(ph < duty, 1.0, -1.0)
def tri(f, dur):
    t = np.arange(int(dur * SR)) / SR; ph = (t * f) % 1; return 4 * np.abs(ph - 0.5) - 1
def saw(f, dur):
    t = np.arange(int(dur * SR)) / SR; return 2 * ((t * f) % 1) - 1
def noise(dur): return rng.uniform(-1, 1, int(dur * SR))
def hp(x, fc): return x - lp(x, fc)

def kick(g=1.0):
    d = 0.32; t = np.arange(int(d * SR)) / SR
    f = 45 + 95 * np.exp(-t * 30); ph = 2 * np.pi * np.cumsum(f) / SR
    return np.sin(ph) * np.exp(-t * 9) * g
def snare():
    d = 0.18; t = np.arange(int(d * SR)) / SR
    n = hp(noise(d), 1200) * np.exp(-t * 22); b = np.sin(2 * np.pi * 190 * t) * np.exp(-t * 30)
    return lp(n, 7000) * 0.7 + b * 0.35
def hat(open_=False):
    d = 0.12 if open_ else 0.04; t = np.arange(int(d * SR)) / SR
    return hp(noise(d), 7000) * np.exp(-t * (18 if open_ else 80))
def pluck(m, dur, duty=0.25, cut=3200):
    s = square(hz(m), dur, duty) * env(int(dur * SR), 0.003, 0.12, 0.35, 0.06)
    return lp(s, cut)
def bassnote(m, dur):
    return tri(hz(m), dur) * env(int(dur * SR), 0.004, 0.08, 0.8, 0.05)
def pad(chord, dur):
    s = sum(saw(hz(m) * (1 + d), dur) for m in chord for d in (-0.004, 0.004))
    return lp(s / (len(chord) * 2), 1400) * env(int(dur * SR), 0.25, 0.3, 0.8, 0.4)

# chords per 2 s bar: Am F C G  (roots, triads)
PROG = [(45, [57, 60, 64]), (41, [57, 60, 65]), (48, [55, 60, 64]), (43, [55, 59, 62])]
def chord_at(t): return PROG[int(t // 2) % 4]
LEAD = [76, 72, 69, 72, 77, 72, 69, 72, 76, 72, 67, 72, 74, 71, 67, 71]  # eighth-note arp, 2 bars

def groove(t0, t1, drums=True, lead=True, bass=True, padv=True, lead_gain=0.13):
    t = t0
    while t < t1 - 1e-6:
        step = int(round(t / 0.25))  # 8th notes
        root, triad = chord_at(t)
        b16 = step % 4
        if drums:
            if step % 4 == 0: put(kick(), t, 0.85)
            if step % 4 == 2 and step % 8 == 6: put(kick(), t, 0.5)
            if step % 4 == 2: put(snare(), t, 0.32)
            put(hat(step % 2 == 1), t, 0.07 if step % 2 == 0 else 0.05, pan=0.3)
        if bass:
            m = root if b16 in (0, 3) else root + 12
            put(bassnote(m, 0.22), t, 0.32)
        if lead:
            put(pluck(LEAD[step % 16] if root != 41 else [77, 72, 69, 72][step % 4], 0.22), t, lead_gain, pan=-0.2)
        if padv and step % 8 == 0:
            put(pad(triad, 2.0), t, 0.16)
        t += 0.25

# --- Scene 1 hook (0-3): three stabs on the text, cube pop, riser into the logo
for i, ts in enumerate([0.3, 0.9, 1.5]):
    put(kick(1.1), ts, 0.9)
    put(pad([57, 60, 64] if i < 2 else [57, 64, 69], 0.5), ts, 0.35)
    put(pluck([69, 72, 76][i], 0.3, 0.5, 2400), ts, 0.16)
put(pad([45, 57, 64], 1.5), 1.5, 0.22)
r = noise(1.2); t_ = np.arange(len(r)) / SR
put(lp(r, 3000) * (t_ / 1.2) ** 2 * 0.5, 1.8, 0.5)               # riser
for k, ts in enumerate(np.arange(2.0, 3.0, 0.125)): put(pluck(69 + (k % 2) * 12, 0.1, 0.5, 2000), ts, 0.05 + k * 0.006)

# --- Scene 2 logo drop (3.0) and full groove from there to the payoff
boom = kick(1.0) * 1.3; put(boom, 3.0, 1.0)
put(lp(noise(1.0), 5000) * np.exp(-np.arange(SR) / SR * 4), 3.0, 0.18)
groove(3.0, 16.5)

# --- Scene 3 team chips: soft ticks in key
for i in range(9):
    ts = 7.7 + i * 0.12; put(pluck([81, 84, 88][i % 3], 0.08, 0.5, 3000), ts, 0.045, pan=0.4)
# VS stamp
put(kick(0.8), 7.0, 0.5); put(hat(True), 7.0, 0.12)

# --- Scene 4 chat blips (same first-appearance times as the video)
G0, G1 = 2280, 7260
def lin(t, a, b): return min(1, max(0, (t - a) / (b - a)))
def gametime(t): return int(np.floor(G0 + (G1 - G0) * lin(t, 11.25, 16.1)))
def nmsgs(gt): return (3 if gt >= 2400 else 0) + (2 if gt >= 3600 else 0) + (2 if gt >= 4800 else 0) + (2 if gt >= 6000 else 0)
prev = 0
for f in range(int(23.5 * 30) + 1):
    t = f / 30; n = nmsgs(gametime(t))
    first = prev
    while prev < n:
        root, triad = chord_at(t)
        put(pluck(triad[prev % 3] + 12, 0.07, 0.125, 2600), t + 0.12 * (prev - first), 0.06, pan=-0.3); prev += 1
# --- Scene 5 payoff: music drops, whoosh as the block falls, impact on bedrock, victory chord
put(pad([45, 52, 57], 0.6), 16.5, 0.14)
w = noise(0.55); tw = np.arange(len(w)) / SR
put(lp(w, 4000) * (tw / 0.55) ** 1.5 * 0.6, 16.62, 0.5)
LAND = 17.15
put(kick(1.3) * 1.4, LAND, 1.0)
put(lp(noise(0.6), 2500) * np.exp(-np.arange(int(0.6 * SR)) / SR * 7), LAND, 0.35)
for m in (60, 64, 67, 72): put(pluck(m, 1.6, 0.5, 2600), LAND + 0.2, 0.07)
put(pad([48, 55, 60, 64], 2.6), LAND + 0.2, 0.35)
for k, m in enumerate([72, 76, 79, 84]): put(pluck(m, 0.25, 0.25, 3500), LAND + 0.25 + k * 0.125, 0.1)
groove(18.0, 20.0, drums=True, lead=False, padv=False)

# --- Scene 6 outro: groove resolves, final A minor chord rings out
groove(20.0, 22.0, lead_gain=0.11)
put(kick(1.1), 22.0, 0.8)
put(pad([45, 57, 60, 64, 69], 1.5), 22.0, 0.3)
for k, m in enumerate([69, 72, 76, 81]): put(pluck(m, 0.6, 0.5, 2400), 22.0 + k * 0.125, 0.08)

# --- master: gentle stereo delay, soft clip, fade, normalize
def delay(x, dt=0.375, fb=0.25):
    y = x.copy(); d = int(dt * SR)
    for k in range(1, 4): y[d * k:] += x[:-d * k] * fb ** k
    return y
L = delay(mixL); R = delay(mixR, 0.25)
st = np.stack([L, R], 1)
st = np.tanh(st * 1.2) / np.tanh(1.2)
fade = np.ones(N); fn = int(0.45 * SR); fade[-fn:] = np.linspace(1, 0, fn); st *= fade[:, None]
st *= 0.89 / np.max(np.abs(st)) * 0.8
pcm = (st * 32767).astype(np.int16)
with wave.open('music.wav', 'wb') as wf:
    wf.setnchannels(2); wf.setsampwidth(2); wf.setframerate(SR); wf.writeframes(pcm.tobytes())
print('ok', pcm.shape)
