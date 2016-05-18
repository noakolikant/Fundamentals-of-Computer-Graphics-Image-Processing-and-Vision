
fittingfn = @homfitfn;
distfn = @homdistfn;
degenfn = @isdegenerate;
t = 0.001;
s = 4; 
feedback = 0;
maxDataTrials = 100;
maxTrials = 10000;

H_ransac = RANSAC_Wrapper(matches, fittingfn, distfn, degenfn, s, t, feedback, maxDataTrials, maxTrials);
new_im_ransac = ComputeProjective(Im, H_ransac); imshow(new_im_ransac)
[ pnts_gt, pnts_computed ] = ComputeTestPoints(H_gt, H_ransac);
ComputeError(pnts_gt, pnts_computed) / 100
